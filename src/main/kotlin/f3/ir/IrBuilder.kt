package f3.ir

import f3.ast.*

class IrHeaderBuilder(val astModules: List<AstModule>) {

    fun getIrType(astModule: AstModule, astReference: AstReference<*>): IrType {
        val isInModule = astModule.name == astReference.moduleName

    }

    fun convert(): List<IrModuleHeader> {
        return astModules.map { convert(it) }
    }

    fun convert(astModule: AstModule): IrModuleHeader {
        val name = astModule.name
        val imports = astModule.imports
        val traits = astModule.traits.map { convert(astModule, it) }
        val structs = astModule.structs.map { convert(astModule, it) }
        val variables = astModule.variables.map { convert(astModule, it) }
        val functions = astModule.functions.map { convert(astModule, it) }

        return IrModuleHeader(name, imports, traits, structs, variables, functions)
    }

    fun convert(astModule: AstModule, astTrait: AstTrait): IrTrait {
        val module = astModule.name
        val name = astTrait.name
        astTrait.fields.map { getIrType(astModule, it.typeName) }
        val functions = astTrait.methods.map { convert(astModule, it) }

        throw NotImplementedError("AstTrait -> IrTrait")
    }

    fun convert(astModule: AstModule, astStruct: AstStruct): IrStructHeader {
        throw NotImplementedError("AstStruct -> IrStructHeader")
    }

    fun convert(astModule: AstModule, astVariable: AstVariable): IrGlobalVariableHeader {
        throw NotImplementedError("AstVariable -> IrGlobalVariableHeader")
    }

    fun convert(astModule: AstModule, astFunction: AstFunction): IrFunctionHeader {
        return convert(astModule, astFunction.functionHeader)
    }

    fun convert(astModule: AstModule, astFunctionHeader: AstFunctionHeader): IrFunctionHeader {
        throw NotImplementedError("AstFunctionHeader -> IrFunctionHeader")
    }

}
