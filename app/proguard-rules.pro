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
  # Add this global rule
    -keepattributes Signature

    # This rule will properly ProGuard all the model classes in
    # the package com.yourcompany.models. Modify to fit the structure
    # of your app.
    -keepclassmembers class com.royalenfield.reprime.models.** {
          *;
        }

-keep class com.fasterxml.jackson.annotation.** { *; }

-dontwarn com.fasterxml.jackson.databind.**

#RETROFIT
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#GLIDE
#If you use proguard, you may need to add the following lines to your proguard.cfg:

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#If you're targeting any API level less than Android API 27, also include:
#```pro
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
#VideoDecoder uses API 27 APIs which may cause proguard warnings even though the newer APIs won’t be called on devices with older versions of Android.

#If you use DexGuard you may also want to include:

#GLIDE OKHTTP
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#FIREBASE/FIRESTORE
# Needed for DNS resolution.  Present in OpenJDK, but not Android
-dontwarn javax.naming.**

# Don't warn about checkerframework
#
# Guava uses the checkerframework and the annotations
# can safely be ignored at runtime.
-dontwarn org.checkerframework.**
-ignorewarnings
# Guava warnings:
-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.annotations.Weakcom.squareup.javapoet
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.lang.model.element.Modifier

# Okhttp warnings.
-dontwarn okio.**
-dontwarn com.google.j2objc.annotations.**

#FIREBASE MESSAGING
# Analytics library is optional.
# Access to this class is protected by try/catch(NoClassDefFoundError e)
# b/35686744 Don't fail during proguard if the class is missing from the APK.
-dontwarn com.google.android.gms.measurement.AppMeasurement*

# We keep all fields for every generated proto file as the runtime uses
# reflection over them that ProGuard cannot detect. Without this keep
# rule, fields may be removed that would cause runtime failures.
-keepclassmembers class * extends com.google.android.gms.internal.measurement.zzyv {
  <fields>;
}


#KOTLIN MVVM
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
-dontwarn kotlin.**

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-verbose
-dontnote
-useuniqueclassmembernames
-printmapping mapping.txt

##### Google #####
# Databinding
-dontwarn android.databinding.**
# Dagger
-dontwarn com.google.errorprone.annotations.**
##################

##### OkHttp, Retrofit #####
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#################

##### Moshi for JSON #####
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
###########################

##### gRPC #####
-dontwarn android.test.**
-dontwarn com.google.common.**
-dontwarn javax.naming.**
-dontwarn okio.**
-dontwarn org.junit.**
-dontwarn org.mockito.**
-dontwarn sun.reflect.**
# Ignores: can't find referenced class javax.lang.model.element.Modifier
-dontwarn com.google.errorprone.annotations.**
##################

##### Protobuf #####
-keep class * extends com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**
####################

##### Crashlytics #####
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
#######################

##### Glide #####
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#################

##### Wasabeef #####
# Protobuf
-keep class jp.wasabeef.data.proto.** { *; }
-keep class jp.wasabeef.data.grpc.** { *; }
##################

#MOCKITO
-dontwarn org.mockito.**

-keep class com.royalenfield.reprime.ui.motorcyclehealthalert.adapter.VehicleHealthAlertAdapter
  -keepclassmembers class com.royalenfield.reprime.ui.phoneconfigurator.models.** {
          *;
        }

          -keepclassmembers class com.royalenfield.reprime.ui.triplisting.view.** {
                  *;
                }
                 -keepclassmembers class com.royalenfield.reprime.ui.triplisting.response.** {
                                  *;
                                }
                  -keepclassmembers class com.royalenfield.reprime.ui.tripdetail.view.** {
                                  *;
                                }

   -keepclassmembers class com.royalenfield.reprime.ui.home.service.diy.interactor.** {
                  *;
                }
            -keepclassmembers class com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.** {
                            *;
                          }

                  -keepclassmembers class com.royalenfield.reprime.ui.home.connected.motorcycle.model.** {
                                            *;
                                          }
  -keepclassmembers class com.royalenfield.reprime.ui.userdatavalidation.popup.models.** {
          *;
        }
         -keepclassmembers class com.royalenfield.reprime.ui.motorcyclehealthalert.models.** {
                  *;
                }
                -keepclassmembers class com.royalenfield.reprime.ui.motorcyclehealthalert.adapter.** {
                                  *;
                                }
   -keepclassmembers class com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.** {
          *;
        }
    -keepclassmembers class   com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.** {
         *;
        }



-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }
-keep class * extends androidx.databinding.DataBinderMapper
-keep class * extends androidx.databinding.DataBinderMapper { *; }
-keep class com.royalenfield.reprime.databinding.HealthAlertFragmentBinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
# Navigation
-verbose

# We should at least keep the Exceptions, InnerClasses,
# and Signature attributes when processing a library.
-keepattributes Exceptions,InnerClasses,Signature,RuntimeVisibleParameterAnnotations,RuntimeVisibleAnnotations

-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn org.apache.oltu.oauth2.**

#Ignoring the warnings
-dontwarn com.squareup.javapoet.**
-dontwarn org.stringtemplate.v4.gui.**
-dontwarn org.antlr.v4.gui.**
-dontwarn org.antlr.runtime.**
-dontwarn org.abego.treelayout.**
-dontwarn org.slf4j.**

#Joda time
# These aren't necessary if including joda-convert, but
# most people aren't, so it's helpful to include it.
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

-dontwarn kotlinx.coroutines.flow.**inlined**
-dontwarn Type_mirror_extKt
-dontwarn Type_mirror_extKt$WhenMappings
-dontwarn butterknife.internal.**
-dontwarn com.google.auto.common.**
-dontwarn com.google.firebase.appindexing.**
-dontwarn com.android.installreferrer.api.**
#-keepattributes JavascriptInterface
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}


-keep class net.danlew.android.joda.R$raw { *; }
-keep class com.google.crypto.tink.** { *; }
-keepclassmembers class * extends com.google.crypto.tink.shaded.protobuf.GeneratedMessageLite {
    <fields>;
}

#Keep class from code obfuscation
-keep class com.royalenfield.reprime.notification.listener.** { *; }
-keepclassmembers class com.royalenfield.reprime.notification.listener.** { *; }

-keep class com.royalenfield.reprime.notification.receiver.** { *; }
-keepclassmembers class com.royalenfield.reprime.notification.receiver.** { *; }

-keep class com.royalenfield.reprime.utils.REConstantsInterface { *; }
-keepclassmembers class com.royalenfield.reprime.utils.REConstantsInterface { *; }

-keep class com.radaee.** { *; }
-keepclasseswithmembernames class com.radaee.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer



