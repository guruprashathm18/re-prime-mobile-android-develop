# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Remove unwanted logs for release builds
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
}

# don't shrink/remove any interface/class from nephele
-dontshrink

-keep class com.bosch.softtec.components.nephele.CloudManager { public *; }
-keep class com.bosch.softtec.components.nephele.CloudManager$Companion { public *; }


-keep class com.bosch.softtec.components.nephele.CloudError { *; }
-keep class com.bosch.softtec.components.nephele.CloudException {
    <fields>;
    public protected *;
}

-keep class com.bosch.softtec.components.nephele.extensions.** { public protected *; }
-keep class com.bosch.softtec.components.nephele.JsonWebToken { *; }

-keep class com.bosch.softtec.components.nephele.CloudConfiguration {
    <fields>;
    public protected <methods>;
}

-keep class com.bosch.softtec.components.nephele.BackendConfiguration {
    <fields>;
    public protected <methods>;
}

-keep class com.bosch.softtec.micro.tenzing.client.model.** {
    <fields>;
    public protected <methods>;
}


# Keep drawables and other resource files
-keep class **.R
-keep class **.R$* {
    <fields>;
}

-dontwarn kotlin.jvm.internal.Reflection
-dontwarn kotlin.internal.PlatformImplementationsKt
-dontnote android.os.Build
-dontnote android.content.res.Configuration
-dontnote android.os.LocaleList
-allowaccessmodification
-optimizationpasses 5
-keepattributes Exceptions,SourceFile,LineNumberTable,RuntimeVisible*Annotation*,AnnotationDefault
-renamesourcefileattribute SourceFile
