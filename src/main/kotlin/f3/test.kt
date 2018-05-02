package f3

import f3.ast.*
import f3.ir.IrHeaderBuilder

fun main(args: Array<String>) {
    val astModule = AstModule(
            listOf("std", "test"),
            listOf<ModuleName>(),
            listOf<AstTrait>(),
            listOf<AstStruct>(
                    AstStruct(
                            "StructA",
                            listOf<AstVariable>(),
                            listOf<AstFunction>(),
                            listOf<AstReference<AstTrait>>())
            ),
            listOf<AstVariable>(),
            listOf<AstFunction>()
    )

    AstResolver.resolve(listOf(astModule))
    val irHeaders = IrHeaderBuilder.convert(listOf(astModule))

    irHeaders.forEach { println(it) }
}
