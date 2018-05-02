package f3.ast

import java.util.*

typealias ModuleName = List<String>

data class AstModule(
        val name: ModuleName,
        val imports: List<ModuleName>,
        val traits: List<AstTrait>,
        val structs: List<AstStruct>,
        val variables: List<AstVariable>,
        val functions: List<AstFunction>
)

/**
 * Reference to an entity that may be in another module
 */
data class AstReference<T>(
        val moduleName: ModuleName,
        val reference: String
) : AstExpression {

    private val lock = Any()

    @Volatile
    var obj: T? = null
        get() {
            synchronized(lock) {
                return field
            }
        }
        set(value) {
            synchronized(lock) {
                field = value
            }
        }

    fun isResolved(): Boolean = obj != null

    override fun equals(other: Any?): Boolean {
        if (other !is AstReference<*>) return false
        return this.moduleName == other.moduleName
                && this.reference == other.reference
                && this.obj == other.obj
    }

    override fun hashCode(): Int {
        return Objects.hash(moduleName, reference, isResolved())
    }

    override fun toString(): String {
        return "${moduleName.joinToString("::")}::$reference (resolved=${isResolved()})"
    }
}

interface AstType {
    val name: String
}

data class AstTrait(
        override val name: String,
        val fields: List<AstVariable>,
        val methods: List<AstFunctionHeader>
) : AstType

data class AstStruct(
        override val name: String,
        val fields: List<AstVariable>,
        val methods: List<AstFunction>,
        val traitsImplemented: List<AstReference<AstTrait>>
) : AstType

// variable or function
interface AstValue {
    val name: String
}

data class AstVariable(
        override val name: String,
        val constant: Boolean = true,
        val typeName: AstReference<AstType>,
        val initialExpression: AstExpression? = null
) : AstStatement, AstValue {
    override val requiredTopLevel: Boolean = true
}

data class AstFunctionHeader(
        val name: String,
        val arguments: List<AstFunctionArgument>,
        val returnType: AstReference<AstType>
)

data class AstFunction(
        val functionHeader: AstFunctionHeader,
        val statements: List<AstStatement>
) : AstStatement, AstValue {
    override val requiredTopLevel: Boolean = true
    override val name: String = functionHeader.name
}

data class AstFunctionArgument(
        val name: String,
        val type: AstReference<AstType>
)

/* -- Expressions -- */

interface AstExpression

interface AstStatement : AstExpression {
    val requiredTopLevel: Boolean
}

data class AstFunctionCallExpression(
        val function: AstExpression,
        val arguments: List<AstExpression>
) : AstExpression

data class AstStructGetExpression(
        val struct: AstExpression,
        val variableName: String
) : AstExpression

data class AstStructSetExpression(
        val struct: AstReference<AstValue>,
        val field: String,
        val expression: AstExpression
) : AstStatement {
    override val requiredTopLevel: Boolean = true
}

data class AstIfExpression(
        val check: AstExpression,
        val statements: List<AstStatement>,
        val elseIfExpression: AstIfExpression? = null
) : AstStatement {
    override val requiredTopLevel: Boolean = false
}

interface AstConstant : AstExpression

data class AstNumberConstant(val value: String) : AstConstant
