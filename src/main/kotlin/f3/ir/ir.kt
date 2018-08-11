package f3.ir

import f3.ast.ModuleName

data class IrModule(
        val name: ModuleName,
        val imports: List<IrModuleHeader>,
        val traits: List<IrTrait>,
        val structs: List<IrStruct>,
        val variables: List<IrGlobalVariable>,
        val functions: List<IrFunction>
)

data class IrModuleHeader(
        val name: ModuleName,
        val imports: List<ModuleName>,
        val traits: List<IrTrait>,
        val structs: List<IrStructHeader>,
        val variables: List<IrGlobalVariableHeader>,
        val functions: List<IrFunctionHeader>
)

data class IrTrait(
        val module: ModuleName,
        val name: String,
        val fields: List<IrType>,
        val functions: List<IrFunctionHeader>
) : IrType

data class IrStruct(
        val module: ModuleName,
        val name: String,
        val traitsImplemented: List<IrTrait>,
        val fields: List<IrType>,
        val methods: List<IrFunction>
) : IrType {
    fun getStructType(): IrStructType {
        return IrStructType(fields)
    }
}

data class IrStructHeader(
        val module: ModuleName,
        val name: String,
        val traitsImplemented: List<IrTrait>,
        val fields: List<IrType>,
        val methods: List<IrFunctionHeader>
) : IrType {
    fun getStructType(): IrStructType {
        return IrStructType(fields)
    }
}

data class IrGlobalVariableHeader(
        val module: ModuleName,
        val name: String,
        val type: IrType
)

data class IrGlobalVariable(
        val module: ModuleName,
        val name: String,
        val type: IrType,
        val value: IrConstant
)

data class IrFunctionHeader(
        val module: ModuleName,
        val name: String,
        val arguments: List<IrType>,
        val result: IrType
) {
    fun getFunctionType(): IrFunctionType {
        return IrFunctionType(arguments, result)
    }
}

data class IrFunction(
        val module: ModuleName,
        val name: String,
        val arguments: List<IrType>,
        val result: IrType,
        val body: List<IrBlock>
) {
    fun getFunctionType(): IrFunctionType {
        return IrFunctionType(arguments, result)
    }
}

data class IrBlock(
        val name: String,
        val instructions: List<IrInstruction>
)

interface IrConstant

data class IrNumberConstant(val value: String) : IrConstant

interface IrInstruction

data class IrJumpInstruction(
        val block: String
) : IrInstruction

data class IrReturnInstruction(
        val value: IrInstruction
) : IrInstruction
