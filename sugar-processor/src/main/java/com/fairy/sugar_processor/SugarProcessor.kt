package com.fairy.sugar_processor

import com.fairy.sugar_annotation.SugarViewHolder
import com.squareup.javapoet.*
import java.lang.Exception
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic


class SugarProcessor : AbstractProcessor() {

    private val METHOD_PREFIX = "start"
    private val classIntent: ClassName = ClassName.get("android.content", "Intent")
    private val classContext: ClassName = ClassName.get("android.content", "Context")
    private val classViewGroup: ClassName = ClassName.get("android.view", "ViewGroup")
    private val classBaseViewHolder: ClassName =
        ClassName.get("", "BaseViewHolder")
    private val classUIModel: ClassName =
        ClassName.get("", "RecyclerViewUIModel")
    private val classViewHolderMapping: ClassName =
        ClassName.get("java.util", "HashMap")

    private var filer: Filer? = null
    private var messager: Messager? = null
    private var elements: Elements? = null
    private var activitiesWithPackage: HashMap<String, String>? = null

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        filer = processingEnvironment?.filer
        messager = processingEnvironment?.messager
        elements = processingEnvironment?.elementUtils
        activitiesWithPackage = HashMap()
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
            activitiesWithPackage?.put(element.simpleName.toString(), qualifiedName)


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
            JavaFile.builder("com.fairy.templateapp.viewholders", viewHolderClass.build()).build()
                .writeTo(filer)

        }

        /**
         * 2- Generate a class
         */
        val navigatorClass = TypeSpec
            .classBuilder("SugarGenerator")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        /*

    public static void registerViewHolders(HashMap<Class<? extends RecyclerViewUIModel>, Class<? extends BaseViewHolder>> viewHoldersMapping) {
        viewHoldersMapping.put(ViewHolderGenerator.EmptyUIModel.class, EmptyUIModelViewHolder.class);
    }
         */

        val mappingMethodBuilder = MethodSpec
            .methodBuilder("registerViewHolders")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(classViewHolderMapping, "viewHoldersMapping")


        /*
            public static BaseViewHolder createViewHolder(ViewGroup parent, Class clazz) {
        if (clazz.equals(ListSimpleUIModel.class)) {
            return new ListSimpleUIModelViewHolder(parent);
        }
        return null;
    }
         */

        val creatorParameter1 = ParameterSpec.builder(classViewGroup, "parent").build()
        val creatorParameter2 = ParameterSpec.builder(ClassName.INT, "viewType").build()

        val creatorMethodBuilder = MethodSpec
            .methodBuilder("createViewHolder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(classBaseViewHolder)
            .addParameters(listOf(creatorParameter1, creatorParameter2))


        var viewType = 1
        for ((viewHolderName, packageName) in activitiesWithPackage!!) {
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
                "com.fairy.templateapp.viewholders",
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