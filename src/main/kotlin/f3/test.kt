package f3

import f3.ast.*
import f3.ir.IrHeaderBuilder

fun main(args: Array<String>) {
    val astModule = AstModule(
            listOf("std", "test"),
            listOf<ModuleName>(),
            listOf<AstTrait>(),
            listOf<AstStruct>(),
            listOf<AstVariable>(),
            listOf<AstFunction>()
    )

    val resolver = AstResolver(astModule, listOf())
    resolver.resolve()

    val irHeaderBuilder = IrHeaderBuilder(listOf(astModule))
    val irHeaders = irHeaderBuilder.convert()

    irHeaders.forEach { println(it) }
}
