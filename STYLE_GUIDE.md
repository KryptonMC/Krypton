# Krypton style guide
This guide serves as a guide for code style when contributing to the Krypton project.
Most of this is banning bad practices and recommendations to avoid additional maintenance burden.

For the most part, we follow the [official Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html), with the following constraints:
* The top-level package is not omitted, and it is a full directory.
* Avoid files with multiple top-level classes, as it can be difficult to name files, and it can also be difficult to find these classes if you don't have an IDE (such as viewing through GitHub).
* Do not use UpperCamelCase for any variable or function declarations, no matter what they are.
* Avoid private backing fields with public overrides, especially fields beginning with an underscore (`_`).

### Banned language features
1. Extension properties. These can be hard to understand, they hide what's actually going on (they're backed by methods), and they often require other techniques like memoization to make them work effectively like fields. Just use a function.
2. Extension functions as members. This is when you place an extension function inside a class or interface, like this:
    ```kotlin
    class MyClass {

        fun String.slice(delimeter: CharSequence): Array<String> = split(delimiter).toTypedArray()
    }
    ```
    This has two receivers, and the syntax for calling this is really strange.
    As a reference, [here is an example of how bad it can get](https://github.com/mworzala/canvas/blob/f74d0e3568a28214056155eef7ec1532e13d21d4/src/main/kotlin/com/mattworzala/canvas/RenderContext.kt#L54). Don't do this. Ever.

### Language syntax recommendations
* Avoid public top-level functions, as the imports for them can be hard to trace back to their source outside of an IDE, and they can lack context. The exception to this is DSL functions.
* Always explicitly declare return types of functions and types of public variables.
* Avoid public top-level constants. The imports can be hard to track outside of an IDE, and they will also lack context.
* Avoid excessive labels. The nesting context can be hard to track.
* Avoid statement expressions or overly complex expressions in string templates.
    These can be hard to read and hard to track, often require confusing wrapping of the strings, and are usually just there to avoid writing one extra line with a variable.
    For example, you can make an if expression in a template a separate variable:
    ```kotlin
    // This should be avoided
    val hello = "Hello ${if (hasSurname) "$firstName:$surname" else firstName}!"
    // This would be better
    val name = if (hasSurname) "$firstName:$surname" else firstName
    val hello = "Hello $name!"
    ```
* Avoid expressions with bodies as expression function return types, as it can sometimes be unclear what something belongs to, especially as the bodies are usually aligned where the function's body would be.
    For example, this should be avoided:
    ```kotlin
    fun getFile(name: String): File? = try {
        File(name)
    } catch (exception: Exception) {
        null
    }
    ```
* Wrap long chained arguments on the `.`, for example:
    ```kotlin
    val myValue = myMap.filter { it.key == "Hello" }
        .map { it.value }
        .toList()
        .firstOrNull { it == "Goodbye" }
    ```
* Avoid nullable primitive types, as they will always result in boxed types, and boxing/unboxing primitives has bad performance.
    All Kotlin types that are wrappers for Java primitives: `Byte`, `Short`, `Int`, `Long`, `Char`, `Float`, `Double`, and `Boolean`.
    For example, these two functions:
    ```kotlin
    fun nonNullNumber(number: Int): Int = number

    fun nullableNumber(number: Int?): Int? = number
    ```
    Map to these methods in Java:
    ```java
    public int nonNullNumber(int number) {
        return number;
    }

    public Integer nullableNumber(Integer number) {
        return number;
    }
    ```
* Always try to use `@Jvm` annotations, such as `@JvmStatic`, `@JvmOverride`, `@JvmField`, etc., especially when writing API code, as the API must be Java friendly as well as Kotlin friendly.

### API-specific rules and recommendations
* All classes, interfaces, functions, and variables must have explicit visibility and types. This is enforced by the compiler.
* All public elements must be documented. No exceptions.
* Try to follow the factory pattern, in which API elements are interfaces with factories for initializers. This allows us the greatest freedom when implementing the chosen API design, and allows the greatest freedom to adapt to a changing Minecraft.
* Try to find the right balance between too much and not enough when designing APIs. Avoid providing many different alternatives to reach the same result, as this will be confusing. Remember that every single thing you add will need to be maintained.
* Try to avoid revealing implementation details in the API, as these constrain the implementation, stopping us from changing how it works without breaking the API.

### Server-specific rules and recommentations
* Avoid stealing code from elsewhere unless the licensing permits it. Always ensure the licensing of the target code permits use in our code.
    Places you can take code from:
    * Bukkit/Spigot/Paper
    * Sponge
    * Minestom
    * BungeeCord
    * Velocity
    * Stack Overflow
    Places you cannot take code from:
    * Vanilla Minecraft
* Try to write automated tests for your implementations, to avoid having to manually test everything every time.
* If you are writing an API implementation, try to explain areas that may seem confusing or weird. Try to document things that cannot easily be understood by reading the code. Try to explain why you chose the implementation you did in your pull request.
