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
- Current version: 0.2.1

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
    implementation "androidx.legacy:legacy-support-v4:$rootProject.legacyVersion"
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
    implementation "com.github.bumptech.glide:glide:$rootProject.glideVersion"
    implementation "com.github.PearKMA:BaseApplication:$rootProject.baseVersion"
    ...
```
- Testing:
```
    testImplementation "junit:junit:$rootProject.junitVersion"
    androidTestImplementation "androidx.test.ext:junit:$rootProject.testExtJunitVersion"
    androidTestImplementation "androidx.test.espresso:espresso-core:$rootProject.espressoVersion"
    androidTestImplementation "com.google.truth:truth:$rootProject.googleTruthVersion"
    testImplementation "com.google.truth:truth:$rootProject.googleTruthVersion"
    androidTestImplementation "androidx.arch.core:core-testing:$rootProject.coreTestingVersion"
```
In your app build.gradle:
```
ext {
        kotlin_version = '1.5.0'

        compileSdkVersion = 30
        minSdkVersion = 23
        targetSdkVersion = 30

        //
        appcompatVersion = '1.3.0'
        baseVersion = '0.2.1'
        constraintlayoutVersion = '2.0.4'
        coreKtxVersion = '1.5.0'
        coreTestingVersion = '2.1.0'
        coroutinesVersion = '1.5.0'
        desugarVersion = '1.1.5'
        dexterVersion = '6.2.2'
        espressoVersion = '3.3.0'
        fragmentVersion = '1.3.4'
        glideVersion = '4.12.0'
        gradleVersion = '4.2.1'
        googleTruthVersion = '1.1.2'
        hiltJetPackVersion = '1.0.0'
        hiltVersion = '2.35'
        junitVersion = '4.13.2'
        legacyVersion = '1.0.0'
        lifecycleVersion = '2.2.0'
        lifecycleKtxVersion = '2.3.1'
        materialVersion = '1.3.0'
        navigationVersion = '2.3.5'
        recyclerViewVersion = '1.2.0'
        roomVersion = '2.3.0'
        sdp_sspVersion = '1.0.6'
        testExtJunitVersion ='1.1.2'
        timberVersion = '4.7.1'
	...
    }
```
## How to use:
### 1. Application:
```
@HiltAndroidApp
class App : Application(){
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
```
### 2. Activity:
```
 @AndroidEntryPoint	// for Hilt
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
- For use navigation:
```
	<androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph" />
```
### 3. Fragment: (similar with Dialog)
```
 @AndroidEntryPoint // for Hilt
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

    override fun onTypeScreen(): TypeScreen {
        return TypeScreen.NONE | TypeScreen.FULL_SCREEN | TypeScreen.NORMAL_SCREEN | TypeScreen.TRANSLUCENT_STATUS_BAR
    }

    override fun handleBackPressed() {
        
    }

    override fun initViews() {
        ... your code
    }

    override fun onActivityReturned(result: ActivityResult) {

    }

    // onStartActivityForResult(intent: Intent, option: ActivityOptionsCompat? = null)
    // onNavigate()/ onNavigateUp()
```
### 4. RecyclerView Adapter
- Support max 1 data variable (item) & 1 listener (listener)
#### 1. Normal adapter
```
val adapter by lazy {
    BaseAdapter<T>(layoutInflater, layoutId).apply {
        listener = this@Fragment
    }
}
adapter.data = data
```
#### 2. List adapter
```
val adapter by lazy {
    BaseListAdapter<T>(diffCallback,layoutInflater, layoutId).apply {
        listener = this@Fragment
    }
}
adapter.submitList() - it?.let(adapter::submitList)
```

### 5. Repository (using Hilt)
```
    class ARepository @Inject constructor(){}
```
With Room:
```
class BRepository @Inject constructor(
    private val itemDao: ItemDao
) {}
```
### 6. ViewModel
```
class ExampleViewModel: BaseViewModel() // or BaseActivityViewModel
```
if using Hilt:
```
@HiltViewModel
class AViewModel @Inject constructor(
    private val ARepository: ARepository,
    private val savedStateHandle: SavedStateHandle
)
```
Register files change:
```
class ExampleViewModel: BaseViewModel(){
    private var contentObserver: ContentObserver? = null
    fun loadData(){
        ...
	if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Files.getContentUri("external")
                ) {
                    loadData()
                }
            }
    }
    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}
/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
private fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}
```
### 7. Room (using Hilt)
#### 1. Model
```
@Entity(tableName = "<name>")
data class A(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: T
) : BaseModel()
```
#### 4. Dao
```
@Dao
interface ItemDao{
     @Query("Select ...") // 
     suspend fun ...
}
```
#### 3. AppDatabase
```
@Database(
    entities = [A::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
```
#### 4. di- AppModule
```
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)
    @Provides
    fun provideItemFavorite(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "<name table>"
    ).build()

    @Singleton
    @Provides
    fun provideItemDao(db: AppDatabase) =
        db.itemDao() // The reason we can implement a Dao for the database
}
```
### 8: Shared Preferrence
```
private lateinit var pref: SharedPreferences
pref = App.prefHelper.pref(requireContext(), <name pref>)
pref.getString(<Key>,<Default>)
pref.edit {
    putString(...)
}
```
### 9: Utils
#### 1. Debounce click
```
View.onDebounceClick(delay){
   //..
}
```
#### 2. View
- check visibility: .isVisible(), .isGone(), .isInvisible()
- set visibility: .visible(), .gone(), .invisible()
- keyboard: EditText.showKeyboard(), EditText.hideKeyboard()
- observer livedata:  .observer(liveData,{})
- Glide: loadNormal, loadCenterCrop, loadRoundedCorner, loadBackground
- time: 
	+video: formatTime, formatFullTime
	+current time(HH:mm:ss): getCurrentTime
	+current date(dd/MM/yyyy_HH/mm/ss): getCurrentDate
	+custom: formatTimePattern(millis, pattern, locale)
#### 3.AudioHelper
```
val audioHelper = AudioHelper(context){ it ->
    true = loss audio
}
```
...
