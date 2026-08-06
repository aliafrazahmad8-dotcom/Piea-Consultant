# PIEA Student ProGuard rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.database.PropertyName <fields>;
}
-keep class com.piea.student.data.model.** { *; }
-dontwarn com.google.errorprone.annotations.**
