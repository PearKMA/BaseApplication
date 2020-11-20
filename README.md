# BaseApplication
Clean and quick implementation of the project (Maybe (╯°□°）╯︵ ┻━┻)

## Getting start:
### Step 1. Add the JitPack repository to your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
### Step 2: Add the dependency:
```
dependencies {
	 implementation 'com.github.PearKMA:BaseApplication:Tag'
}
```
### Expand:
#### 1. For use navigation & hilt: Add to your root build.gradle dependencies
```
classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
classpath "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
```
#### 2. Add plugins to your app build.gradle
```
id 'kotlin-kapt'
id 'kotlin-android-extensions'
id 'androidx.navigation.safeargs.kotlin'
id 'dagger.hilt.android.plugin'
```
#### 3. Enable data binding
```
buildFeatures {
   dataBinding = true
}
```
#### 4. In your app build.gradle dependencies, add yours:
e.g:
```
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    kapt "androidx.room:room-compiler:$rootProject.roomVersion"
    kapt "com.github.bumptech.glide:compiler:$rootProject.glideVersion"
    kapt "com.google.dagger:hilt-android-compiler:$rootProject.hiltVersion"
    kapt "androidx.hilt:hilt-compiler:$rootProject.hiltJetPackVersion"
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation "androidx.core:core-ktx:$rootProject.coreKtxVersion"
    implementation "androidx.appcompat:appcompat:$rootProject.appcompatVersion"
    implementation "com.google.android.material:material:$rootProject.materialVersion"
    implementation "androidx.constraintlayout:constraintlayout:$rootProject.constraintlayoutVersion"
    implementation "com.intuit.sdp:sdp-android:$rootProject.sdp_sspVersion"
    implementation "com.intuit.ssp:ssp-android:$rootProject.sdp_sspVersion"
    implementation "androidx.fragment:fragment-ktx:$rootProject.fragmentVersion"
    implementation "androidx.lifecycle:lifecycle-extensions:$rootProject.lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$rootProject.lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$rootProject.lifecycleVersion"
    implementation "androidx.navigation:navigation-fragment-ktx:$rootProject.navigationVersion"
    implementation "androidx.navigation:navigation-ui-ktx:$rootProject.navigationVersion"
    implementation "androidx.recyclerview:recyclerview:$rootProject.recyclerViewVersion"
    implementation "androidx.room:room-runtime:$rootProject.roomVersion"
    implementation "androidx.room:room-ktx:$rootProject.roomVersion"
    implementation "androidx.viewpager2:viewpager2:$rootProject.viewPagerVersion"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$rootProject.coroutinesVersion"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$rootProject.coroutinesVersion"
    implementation "com.google.dagger:hilt-android:$rootProject.hiltVersion"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:$rootProject.hiltJetPackVersion"
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    implementation 'com.github.PearKMA:BaseApplication:0.1.1'
```
## How to use:
### 1. Activity:
```
 class ExampleActivity : BaseActivity<ActivityExampleBinding>(){
     override fun getLayoutId(): Int {
        return R.layout.activity_example
    }

    override fun isSingleTask(): Boolean {
        return true //for single activity
    }

    override fun initEvents() {
        ... your code
    }

    override fun initViews() {
        ... your code
    }
 }
```
### 2. Fragment:
```
 class ExampleFragment : BaseFragment<FragmentExampleBinding>() {
     override fun getLayoutId(): Int {
        return R.layout.fragment_example
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE   //status bar color
    }

    override fun isDarkTheme(): Boolean {
        return false    // true: change color of status bar text to white, false: change color to black
    }

    override fun handleBackPressed() {
        
    }

    override fun initViews() {
        ... your code
    }
```
### 3. ViewModel
```
class ExampleViewModel: BaseViewModel() // or BaseActivityViewModel

Updating...
