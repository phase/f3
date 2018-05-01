package f3.ir

import f3.ast.*

class IrHeaderBuilder(val astModules: List<AstModule>) {

    val irTypeCache = mutableMapOf<Pair<ModuleName, String>, IrType>()

    fun <T : AstType> getIrType(currentModule: AstModule, astReference: AstReference<T>): IrType {
        // check primitive
        // (maybe check for module?)
        IrPrimitiveTypes.list.find { it.name == astReference.reference }?.let {
            return it
        }

        // check cache
        val pair = Pair(astReference.moduleName, astReference.reference)
        irTypeCache[pair]?.let { return it }

        val isInModule = currentModule.name == astReference.moduleName
        val module = if (isInModule) currentModule else astModules.find { it.name == astReference.moduleName }
                ?: throw IllegalArgumentException("Couldn't find module ${astReference.moduleName}")

        val ref = astReference.obj
        val irType: IrType = when (ref) {
            is AstTrait -> {
                convert(module, ref)
            }
            is AstStruct -> {
                convert(module, ref)
            }
            else -> throw IllegalArgumentException("$ref is a ${ref?.javaClass}")
        }
        irTypeCache[pair] = irType

        return irType
    }

    fun convert(): List<IrModuleHeader> {
        return astModules.map { convert(it) }
    }

    fun convert(astModule: AstModule): IrModuleHeader {
        val name = astModule.name
        val imports = astModule.imports
        val traits = astModule.traits.map { convert(astModule, it) }
        val structs = astModule.structs.map { convert(astModule, it) }
        val variables = astModule.variables.map { convertGlobalVariable(astModule, it) }
        val functions = astModule.functions.map { convert(astModule, it) }

        return IrModuleHeader(name, imports, traits, structs, variables, functions)
    }

    fun convert(astModule: AstModule, astTrait: AstTrait): IrTrait {
        val moduleName = astModule.name
        val name = astTrait.name
        val fields = astTrait.fields.map { getIrType(astModule, it.typeName) }
        val functions = astTrait.methods.map { convert(astModule, it) }

        return IrTrait(moduleName, name, fields, functions)
    }

    fun convert(astModule: AstModule, astStruct: AstStruct): IrStructHeader {
        val moduleName = astModule.name
        val name = astStruct.name
        val traitsImplemented = astStruct.traitsImplemented.map { getIrType(astModule, it) as IrTrait }
        val fields = astStruct.fields.map { getIrType(astModule, it.typeName) }
        val methods = astStruct.methods.map { convert(astModule, it) }

        return IrStructHeader(moduleName, name, traitsImplemented, fields, methods)
    }

    fun convertGlobalVariable(astModule: AstModule, astVariable: AstVariable): IrGlobalVariableHeader {
        val moduleName = astModule.name
        val name = astVariable.name
        val type = getIrType(astModule, astVariable.typeName)

        return IrGlobalVariableHeader(moduleName, name, type)
    }

    fun convert(astModule: AstModule, astFunction: AstFunction): IrFunctionHeader {
        return convert(astModule, astFunction.functionHeader)
    }

    fun convert(astModule: AstModule, astFunctionHeader: AstFunctionHeader): IrFunctionHeader {
        val moduleName = astModule.name
        val name = astFunctionHeader.name
        val arguments = astFunctionHeader.arguments.map { getIrType(astModule, it.type) }
        val result = getIrType(astModule, astFunctionHeader.returnType)

        return IrFunctionHeader(moduleName, name, arguments, result)
    }

}
