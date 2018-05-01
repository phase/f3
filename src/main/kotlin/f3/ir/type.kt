package f3.ir

interface IrType

data class IrPrimitiveType(val name: String) : IrType

object IrPrimitiveTypes {
    val Int32 = IrPrimitiveType("Int32")
    val Boolean = IrPrimitiveType("Boolean")

    val list by lazy {
        listOf(Int32, Boolean)
    }
}

data class IrFunctionType(val arguments: List<IrType>, val result: IrType) : IrType

data class IrStructType(val fields: List<IrType>) : IrType
