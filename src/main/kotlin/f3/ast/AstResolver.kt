package f3.ast

class AstResolver(val module: AstModule, val linkedModules: List<AstModule>) {

    val allModules: List<AstModule> by lazy {
        val modules = mutableListOf(module)
        modules.addAll(linkedModules)
        modules
    }

    fun resolve() {
        module.variables.forEach { resolve(it) }
        module.traits.forEach { resolve(it) }
        module.structs.forEach { resolve(it) }
    }

    inline fun <reified T> resolve(astReference: AstReference<T>) {
        val module = allModules.find { astReference.moduleName == it.name }
                ?: throw IllegalArgumentException("Can't find module ${astReference.moduleName}")

        when (T::class) {
            AstType::class -> {
                val types = mutableListOf<AstType>(*module.structs.toTypedArray())
                types.addAll(module.traits)
                types.find { astReference.reference == it.name }?.let {
                    astReference.obj = it as T
                }
            }
            AstTrait::class -> {
                module.traits.find { astReference.reference == it.name }?.let {
                    astReference.obj = it as T
                }
            }
            AstValue::class -> {
                module.variables.find { astReference.reference == it.name }?.let {
                    astReference.obj = it as T
                } ?: module.functions.find { astReference.reference == it.name }?.let {
                    astReference.obj = it as T
                }
            }
        }

        astReference.obj ?: throw IllegalArgumentException("Can't find reference $astReference")
    }

    fun resolve(astTrait: AstTrait) {
        astTrait.fields.forEach { resolve(it) }
        astTrait.methods.forEach { resolve(it) }
    }

    fun resolve(astStruct: AstStruct) {
        astStruct.traitsImplemented.forEach { resolve(it) }
        astStruct.fields.forEach { resolve(it) }
        astStruct.methods.forEach { resolve(it) }
    }

    fun resolve(astVariable: AstVariable) {
        resolve(astVariable.typeName)
    }

    fun resolve(astFunctionHeader: AstFunctionHeader) {
        astFunctionHeader.arguments.forEach { resolve(it.type) }
        resolve(astFunctionHeader.returnType)
    }

    fun resolve(astFunction: AstFunction) {
        resolve(astFunction.functionHeader)
        astFunction.statements.forEach { resolve(it) }
    }

    fun resolve(astStatement: AstStatement) {

    }

}
