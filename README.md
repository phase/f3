third iteration of my language

Wants:
* Memory Safety of Rust
* Reliability of Erlang
* Concurrency of Pony
* Purity of Haskell
* Speed of C

Needs:
* Flexible Type System
    * Structs & Traits & Generics
        * No ["3 ways to do generics"](https://github.com/rust-lang/rfcs/blob/master/text/1522-conservative-impl-trait.md)
    * Union Types
        * _No_ nulls.
        * Check Pony's _Primitive Types_. (0 size structs?)
    * Enums & Type Aliases
        * Aliasing can be great for APIs.
    * Tuples
        * Really just anonymous structs?
    * Actors for concurrent computing
        * Research how this can be implemented with lifetimes
    * [Maybe Existential Types?](https://www.cakesolutions.net/teamblogs/existential-types-in-scala)
* Good Tooling
    * Look at what Go is doing, do not do what Go is doing.
    * Automatic Semantic Versioning
    * Look at what JavaScript is doing, do not do what JavaScript is doing.
        * lol NPM is garbage
    * Caching package manager with a focus on security.
        * lol NPM is still garbage

Questions that need to be asked:
* Are actors possible with no GC?
* Is a Permissions System viable for real world development?
    * Spamming `+IO` on all of your methods can be annoying,
    but we can create things to remedy this.
        * "Debug prints don't need `+IO`" for example.
* Are explicit allocators viable? (probably yes)
* How do all the parts tie together?

Compiler:
* First prototype compiler will probably be written in Kotlin.
* Then some native language like Rust.
* Then itself (if I get there).
* AST -> IR -> Backends
* Parallel AST resolution
* Backends:
    * LLVM
    * IR Interpreter
    * WASM (maybe just use LLVM?)
    * JVM?? (would require a lot of effort to get working with existing Java libraries)

Syntax:
* C like. Experimental syntaxes never lead anywhere good.
* No cruft, semicolons need to go
* thing: Type

```c
struct X {
    # No fields can be declared after the function definitions start
    x: Int32
    y: In32

    # this would need a mutable reference of 'self'
    @mut
    fun doSomething() {
        self.x = 7
    }
}

actor Y {
    behave runAsync() {
        # runs code async but everything within an actor is sync
    }
}
```
