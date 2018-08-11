package f3

import f3.ast.*
import f3.ir.IrHeaderBuilder

fun main(args: Array<String>) {
    val modA = AstModule(
            listOf("test", "a"),
            listOf<ModuleName>(),
            listOf<AstTrait>(),
            listOf<AstStruct>(
                    AstStruct(
                            "StructA",
                            listOf<AstVariable>(),
                            listOf<AstFunction>(),
                            listOf<AstReference<AstTrait>>(
                                    AstReference(listOf("test", "b"), "TraitB")
                            )
                    )
            ),
            listOf<AstVariable>(),
            listOf<AstFunction>()
    )

    val modB = AstModule(
            listOf("test", "b"),
            listOf<ModuleName>(),
            listOf<AstTrait>(
                    AstTrait(
                            "TraitB",
                            listOf<AstVariable>(),
                            listOf<AstFunctionHeader>()
                    )
            ),
            listOf<AstStruct>(
                    AstStruct(
                            "StructB",
                            listOf<AstVariable>(),
                            listOf<AstFunction>(),
                            listOf<AstReference<AstTrait>>(
                                    AstReference(listOf("test", "b"), "TraitB")
                            )
                    )
            ),
            listOf<AstVariable>(),
            listOf<AstFunction>(
                    AstFunction(
                            AstFunctionHeader(
                                    "funA",
                                    listOf<AstFunctionArgument>(
                                            AstFunctionArgument(
                                                    "x",
                                                    AstReference(listOf("test", "a"), "StructA")
                                            )
                                    ),
                                    AstReference(listOf("test", "a"), "StructA")
                            ),
                            listOf<AstStatement>()
                    )
            )
    )

    val modules = listOf(modA, modB)
    println("--- Unresolved AST Modules")
    modules.forEach { println(it) }

    println("--- Resolved AST Modules")
    AstResolver.resolve(modules)
    modules.forEach { println(it) }

    println("--- IR Modules")
    val irHeaders = IrHeaderBuilder.convert(modules)
    irHeaders.forEach { println(it) }
}
