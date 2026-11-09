# Odometer Library Consumer Rules

# Keep Hilt/Dagger generated code for internal classes
-keepclassmembers,allowobfuscation class * {
    @javax.inject.Inject <init>(...);
}

# Preserve the library's package name for stack traces and logging
-keepnames public class dev.giste.odometer.** { *; }

# DataStore and related serialization
-keepclassmembers class dev.giste.odometer.domain.** { *; }
