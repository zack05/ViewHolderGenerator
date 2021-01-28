package com.fairy.sugar_processor

import com.fairy.sugar_annotation.SugarViewHolder
import com.squareup.javapoet.*
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic
import kotlin.reflect.KClass


class SugarProcessor : AbstractProcessor() {
    private val classViewGroup: ClassName = ClassName.get("android.view", "ViewGroup")
    private val classBaseViewHolder: ClassName =
        ClassName.get("com.fairy.viewholdergenerator", "BaseViewHolder")
    private val classUIModel: ClassName =
        ClassName.get("com.fairy.viewholdergenerator", "RecyclerViewUIModel")
    private val classViewHolderMapping: ClassName = ClassName.get("java.util", "HashMap")

    private var filer: Filer? = null
    private var messager: Messager? = null
    private var elements: Elements? = null
    private var annotatedClasses: HashMap<String, String>? = null

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        filer = processingEnvironment?.filer
        messager = processingEnvironment?.messager
        elements = processingEnvironment?.elementUtils
        annotatedClasses = HashMap()

        val testObject = com.squareup.kotlinpoet.TypeSpec.objectBuilder("ViewHolderGenerator")

        /*

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == -1) return EmptyUIModelViewHolder(parent)
        return SugarGenerator.createViewHolder(parent, viewType) ?: EmptyUIModelViewHolder(parent)
    }
         */

        val mViewHoldersViewTypes = PropertySpec.builder(
            "mViewHoldersViewTypes",
            kotlin.collections.HashMap::class.asClassName().parameterizedBy(
                Class::class.asClassName()
                    .parameterizedBy(TypeVariableName("out RecyclerViewUIModel")),
                Int::class.asTypeName()
            ),
            KModifier.PRIVATE
        ).delegate(
            CodeBlock.builder().beginControlFlow("lazy")
                .add("hashMapOf<Class<out RecyclerViewUIModel>, Int>()")
                .endControlFlow()
                .build()
        ).build()

        testObject.addProperty(mViewHoldersViewTypes)
        testObject.addFunction(
            FunSpec.builder("init")
                .addCode("SugarGenerator.registerViewHolders(mViewHoldersViewTypes)").build()
        )
        testObject.addFunction(
            FunSpec.builder("getViewType")
                .addParameter(
                    com.squareup.kotlinpoet.ParameterSpec(
                        "obj",
                        KClass::class.asClassName()
                            .parameterizedBy(TypeVariableName("out RecyclerViewUIModel"))
                    )
                )
                .addCode("return mViewHoldersViewTypes[obj.java] ?: -1")
                .build()
        )

        testObject.addFunction(
            FunSpec.builder("createViewHolder")
                .addParameter(
                    com.squareup.kotlinpoet.ParameterSpec(
                        "parent",
                        com.squareup.kotlinpoet.ClassName.bestGuess("android.view.ViewGroup")
                    )
                )
                .addParameter(
                    com.squareup.kotlinpoet.ParameterSpec(
                        "viewType",
                        Int::class.asClassName()
                    )
                ).returns(com.squareup.kotlinpoet.ClassName.bestGuess("com.fairy.viewholdergenerator.BaseViewHolder")).addCode(
                    """
        if (viewType == -1) return EmptyUIModelViewHolder(parent)
        return SugarGenerator.createViewHolder(parent, viewType) ?: EmptyUIModelViewHolder(parent)
                """.trimIndent()
                ).build()
        )
        FileSpec
            .builder("com.fairy.viewholdergenerator", "ViewHolderGenerator")
            .addType(testObject.build())
            .build()
            .writeTo(filer!!)
    }

    override fun process(set: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        val annotatedClasses = roundEnv?.getElementsAnnotatedWith(SugarViewHolder::class.java)
            ?: return false

        annotatedClasses.forEach { element ->
            if (element.kind != ElementKind.CLASS) {
                messager?.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.")
                return true
            }
            element as TypeElement
            val qualifiedName = elements?.getPackageOf(element)?.qualifiedName.toString()
            this.annotatedClasses?.put(element.simpleName.toString(), qualifiedName)


            /**
             * 2- Generate a view holder
             */
            val viewHolderClass = TypeSpec
                .classBuilder("${element.simpleName}ViewHolder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(classBaseViewHolder)

            val layoutRes = element.getAnnotation(SugarViewHolder::class.java).layoutRes
            val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(classViewGroup, "parent")
            if (layoutRes == -1) {
                constructor.addStatement("super(parent)")
            } else {
                constructor.addStatement("super(parent, $layoutRes)")
            }

            val bindMethod = MethodSpec
                .methodBuilder("onBind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(classUIModel, "data")
                .addStatement("data.onBind(itemView)")
                .addAnnotation(Override::class.java)

            viewHolderClass.addMethod(constructor.build())
            viewHolderClass.addMethod(bindMethod.build())

            /**
             * 3- Write generated class to a file
             */
            JavaFile.builder("com.fairy.viewholdergenerator", viewHolderClass.build())
                .build()
                .writeTo(filer)

        }

        /**
         * 2- Generate a class
         */
        val navigatorClass = TypeSpec
            .classBuilder("SugarGenerator")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        val mappingMethodBuilder = MethodSpec
            .methodBuilder("registerViewHolders")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(classViewHolderMapping, "viewHoldersMapping")

        val creatorParameter1 = ParameterSpec.builder(classViewGroup, "parent").build()
        val creatorParameter2 = ParameterSpec.builder(ClassName.INT, "viewType").build()

        val creatorMethodBuilder = MethodSpec
            .methodBuilder("createViewHolder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(classBaseViewHolder)
            .addParameters(listOf(creatorParameter1, creatorParameter2))


        var viewType = 1
        for ((viewHolderName, packageName) in this.annotatedClasses!!) {
            val viewHolderClass = ClassName.get(packageName, viewHolderName)

            mappingMethodBuilder
                .addStatement("viewHoldersMapping.put(\$T.class,$viewType)", viewHolderClass)

            creatorMethodBuilder.addStatement(
                "if (viewType == $viewType) return new ${viewHolderClass.simpleName()}ViewHolder(parent)"
            )
            viewType += 1
        }
        creatorMethodBuilder.addStatement("return null")
        navigatorClass.addMethod(mappingMethodBuilder.build())
        navigatorClass.addMethod(creatorMethodBuilder.build())
        /**
         * 3- Write generated class to a file
         */

        try {
            JavaFile.builder(
                "com.fairy.viewholdergenerator",
                navigatorClass.build()
            ).build()
                .writeTo(filer)

        } catch (e: Exception) {

        }


        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(SugarViewHolder::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}