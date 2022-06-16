# BaseApplication

Clean and quick implementation of the project (Maybe (╯°□°）╯︵ ┻━┻)

## Getting start:

### Download repository and import this as module

### Expand:

#### 1. Import buildSrc:

- Type in terminal:

```
mkdir "buildSrc/src/main/java"
type NUL > buildSrc/build.gradle.kts
```

- Write to build.gradle.kts:

```
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}
```

- Copy all files from base's buildSrc

#### 2. Import base module

- Option: import in settings.gradle

```
include ':baselibrary'
project(':baselibrary').projectDir=new File('path_to\\BaseApplication\\baselibrary')
```

- Add module to app:

```
implementation project(':baselibrary')
```

#### 3. Implement root build.gradle:

```
plugins {
    id 'com.android.application' version '7.2.1' apply false
    id 'com.android.library' version '7.2.1' apply false
    id 'org.jetbrains.kotlin.android' version '1.7.0' apply false
    id 'com.github.ben-manes.versions' version '0.42.0'
    id 'androidx.navigation.safeargs.kotlin' version '2.4.2' apply false
    id 'com.google.dagger.hilt.android' version '2.42' apply false
//    id 'com.google.gms.google-services' version '4.3.10' apply false // ready to launch
//    id 'com.google.firebase:firebase.crashlytics.gradle' version '2.8.1' apply false
}
```
!!! if get error (eg. Plugin id contains invalid char ':') , add this to settings.gradle and remove
plugin above

```
pluginManagement {
	...
	resolutionStrategy {
        eachPlugin {
            if (requested.id.id == 'com.google.firebase.crashlytics') {
                useModule("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
            }
           ...
        }
    }
}
```

#### 4. In your app build.gradle dependencies, add yours:

```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs.kotlin'
    id 'dagger.hilt.android.plugin'
    id 'kotlin-parcelize'
//    id 'com.google.gms.google-services'
//    id 'com.google.firebase.crashlytics'
    id 'com.base.plugin'
}

compileSdk Versions.compileSdk
minSdk Versions.minSdk
targetSdk Versions.targetSdk
versionName Const.versionName
    
buildFeatures {
    dataBinding = true
}
    
compileOptions {
    coreLibraryDesugaringEnabled true
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
kotlinOptions {
    jvmTarget = '1.8'
    freeCompilerArgs += Args.coroutines
}
applicationVariants.all { variant ->
    variant.outputs.all {
        outputFileName = "name_${variant.buildType.name}_${defaultConfig.versionName}.apk"
    }
}
    
dependencies {
    coreLibraryDesugaring Deps.desugar
    kapt Kapt.hiltKapt
    kapt Kapt.glideKapt
    kapt Kapt.roomKapt

    implementation Deps.coreKtx
    implementation Deps.appcompat
    implementation Deps.material
    implementation Deps.constraintlayout
    implementation Deps.sdp
    implementation Deps.ssp
    implementation Deps.fragmentKtx
    implementation Deps.livedata
    implementation Deps.viewmodel
    implementation Deps.lifecycleRuntime
    implementation Deps.navigationFragment
    implementation Deps.navigationUi
    implementation Deps.recyclerview
    implementation Deps.coroutines
    implementation Deps.coroutinesCore
    implementation Deps.glide
    implementation Deps.datastore
    implementation Deps.timber
    implementation Deps.retrofit
    implementation Deps.gson
    implementation platform(Deps.firebase)
    implementation Deps.crashlytics
    implementation Deps.analytics
    implementation Deps.configs

    implementation Deps.hilt
    implementation Deps.roomRuntime
    implementation Deps.roomKtx
    implementation Deps.swipeRefreshLayout
    implementation Deps.lottie

	// unit test
    testImplementation Deps.junit
    androidTestImplementation Deps.androidJunit
    androidTestImplementation Deps.espresso
    androidTestImplementation Deps.androidTruth
    testImplementation Deps.truth
    androidTestImplementation Deps.coreTest
    ...
}

kapt {
    correctErrorTypes true
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
    override fun enableDarkMode(): Boolean {
        return false //for single activity
    }

    override fun initBeforeCreateViews(savedInstanceState: Bundle?) {
        ... your code
    }

    override fun initViews(savedInstanceState: Bundle?) {
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
        return TypeScreen...
    }

    override fun handleBackPressed() {
        
    }

    override fun initViews(savedInstanceState: Bundle?) {
        ... your code
    }

    override fun onActivityReturned(result: ActivityResult) {

    }

    // onStartActivityForResult(intent: Intent, option: ActivityOptionsCompat? = null)
    // onNavigate()/ onNavigateUp()
```
### 4. RecyclerView Adapter

- Support max 1 data variable (item) & 1 listener (listener) (custom it if you want more)

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
        
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSoundApi(retrofit: Retrofit): APIService =
        retrofit.create(APIService::class.java)
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

### 9: PreferencesManager

```
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")
    private val mContext = context

    val scheduleFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SCHEDULE_KEY] ?: false
        }

    suspend fun updateSchedule() {
        mContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SCHEDULE_KEY] = true
        }
    }

    private object PreferencesKeys {
        val SCHEDULE_KEY = booleanPreferencesKey("schedule_key")
    }
}

```

### 10: Utils

#### 1. Debounce click

```
View.onDebounceClick(delay){
   //..
}
```

#### 2. View

- set visibility: .show(), .gone(), .hide()
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
