# Android Platform Guidelines

> Material Design 3 essentials, Android design conventions, Roboto typography, and native patterns.
> **Read this file when building for Android devices.**
>
> **v2 — Corrections:** Fixed MD3 dark theme colors (was MD2), Roboto Flex API level (was API 33, is API 31),
> removed React Native code block, replaced deprecated SwipeRefreshLayout with Compose PullToRefresh,
> added Dynamic Color implementation, added Window Size Classes implementation, added Navigation Compose,
> added predictive back implementation, added Material Symbols setup, added full Jetpack Compose section.

---

## 1. Material Design 3 Philosophy

### Core Material Principles

> ✏️ CHANGED: Removed "Material as Metaphor" — that is MD1/MD2 language.
> MD3 brand pillars are Personal, Adaptive, Expressive.

```
MD3 BRAND PILLARS:
├── Personal: Dynamic color adapts to each user's wallpaper
├── Adaptive: One design language across all form factors
└── Expressive: Bold, graphic, intentional design choices

DESIGN VALUES:
├── Surfaces exist in 3D space (light and shadow define hierarchy)
├── Motion provides continuity between states
└── Accessibility is built in, not bolted on
```

### Material Design Values

| Value | Implementation |
|-------|----------------|
| **Dynamic Color** | Colors adapt to wallpaper/user preference |
| **Personalization** | User-specific themes via Material You |
| **Accessibility** | Built into every component |
| **Responsiveness** | Works on all screen sizes |
| **Consistency** | Unified design language across Android |

---

## 2. Android Typography

### Roboto Font Family

> ✏️ CHANGED: Roboto Flex API level corrected — ships with Android 12 (API 31), not API 33.

```
Android System Fonts:
├── Roboto: Default sans-serif
├── Roboto Flex: Variable font (API 31+, Android 12+)
├── Roboto Serif: Serif alternative
├── Roboto Mono: Monospace
└── Google Sans: Google products (special license)
```

### Material Type Scale

| Role | Size | Weight | Line Height | Usage |
|------|------|--------|-------------|-------|
| **Display Large** | 57sp | Regular | 64sp | Hero text, splash |
| **Display Medium** | 45sp | Regular | 52sp | Large headers |
| **Display Small** | 36sp | Regular | 44sp | Medium headers |
| **Headline Large** | 32sp | Regular | 40sp | Page titles |
| **Headline Medium** | 28sp | Regular | 36sp | Section headers |
| **Headline Small** | 24sp | Regular | 32sp | Subsections |
| **Title Large** | 22sp | Regular | 28sp | Dialogs, cards |
| **Title Medium** | 16sp | Medium | 24sp | Lists, navigation |
| **Title Small** | 14sp | Medium | 20sp | Tabs, secondary |
| **Body Large** | 16sp | Regular | 24sp | Primary content |
| **Body Medium** | 14sp | Regular | 20sp | Secondary content |
| **Body Small** | 12sp | Regular | 16sp | Captions |
| **Label Large** | 14sp | Medium | 20sp | Buttons, FAB |
| **Label Medium** | 12sp | Medium | 16sp | Navigation |
| **Label Small** | 11sp | Medium | 16sp | Chips, badges |

### Scalable Pixels (sp)

```
sp = Scale-independent pixels

sp automatically scales with:
├── User font size preference
├── Display density
└── Accessibility settings

RULE: ALWAYS use sp for text, dp for everything else.
```

### Font Weight Usage

| Weight | Use Case |
|--------|----------|
| Regular (400) | Body text, display |
| Medium (500) | Buttons, labels, emphasis |
| Bold (700) | Rarely, strong emphasis only |

---

## 3. Material Color System

### Dynamic Color (Material You)

> ✏️ ADDED: Implementation code — the original had none.

```
Android 12+ Dynamic Color:

User's wallpaper → Color extraction → App theme

Your app automatically adapts to:
├── Primary color (from wallpaper)
├── Secondary color (complementary)
├── Tertiary color (accent)
├── Surface colors (derived)
└── All semantic colors adjust

RULE: Implement dynamic color for personalized feel.
```

**Compose implementation:**

```kotlin
// In your Theme.kt
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic color requires Android 12+ (API 31)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        // Fallback for Android 11 and below
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
```

### Semantic Color Roles

```
Surface Colors:
├── Surface → Main background
├── SurfaceVariant → Cards, containers
├── SurfaceTint → Elevation overlay
├── InverseSurface → Snackbars, tooltips

On-Surface Colors:
├── OnSurface → Primary text
├── OnSurfaceVariant → Secondary text
├── Outline → Borders, dividers
├── OutlineVariant → Subtle dividers

Primary Colors:
├── Primary → Key actions, FAB
├── OnPrimary → Text on primary
├── PrimaryContainer → Less emphasis
├── OnPrimaryContainer → Text on container

Secondary/Tertiary: Similar pattern
```

### Error, Warning, Success Colors

| Role | Light | Dark | Usage |
|------|-------|------|-------|
| Error | #B3261E | #F2B8B5 | Errors, destructive |
| OnError | #FFFFFF | #601410 | Text on error |
| ErrorContainer | #F9DEDC | #8C1D18 | Error backgrounds |

> Note: These are MD3 baseline values. If dynamic color is enabled (API 31+),
> the color scheme at runtime will be generated from the user's wallpaper and
> these baseline values will be replaced automatically.

### Dark Theme

> ✏️ CHANGED: Corrected background colors — #121212 is Material Design 2.
> MD3 dark theme uses different surface values.

```
Material 3 Dark Theme:

├── Background: #141218 (not #121212 — that is MD2)
├── Surface: #1C1B1F
├── SurfaceVariant: #49454F
├── Elevation: Higher = lighter tonal overlay (not alpha overlay)
├── Reduce saturation on colors
└── Check contrast ratios against MD3 values

MD3 elevation uses tonal color (primary tint), not alpha overlays:
├── Level 0 (0dp)  → Surface only
├── Level 1 (1dp)  → Surface + 5%  primary
├── Level 2 (3dp)  → Surface + 8%  primary
├── Level 3 (6dp)  → Surface + 11% primary
├── Level 4 (8dp)  → Surface + 12% primary
├── Level 5 (12dp) → Surface + 14% primary
```

---

## 4. Android Layout & Spacing

### Layout Grid

```
Android uses 8dp baseline grid:

All spacing in multiples of 8dp:
├── 4dp: Component internal (half-step)
├── 8dp: Minimum spacing
├── 16dp: Standard spacing
├── 24dp: Section spacing
├── 32dp: Large spacing

Margins:
├── Compact (phone): 16dp
├── Medium (small tablet): 24dp
├── Expanded (large): 24dp+ or columns
```

### Responsive Layout

```
Window Size Classes:

COMPACT (< 600dp width):
├── Phones in portrait
├── Single column layout
├── Bottom navigation

MEDIUM (600-840dp width):
├── Tablets, foldables
├── Consider 2 columns
├── Navigation rail option

EXPANDED (> 840dp width):
├── Large tablets, desktop
├── Multi-column layouts
├── Navigation drawer
```

> ✏️ ADDED: Compose implementation for window size classes.

**Compose implementation:**

```kotlin
// Dependency: androidx.compose.material3:material3-window-size-class
@Composable
fun MyApp() {
    val windowSizeClass = calculateWindowSizeClass(this) // in Activity
    AppContent(windowSizeClass)
}

@Composable
fun AppContent(windowSizeClass: WindowSizeClass) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Bottom navigation, single column
            CompactLayout()
        }
        WindowWidthSizeClass.Medium -> {
            // Navigation rail, optional 2-column
            MediumLayout()
        }
        WindowWidthSizeClass.Expanded -> {
            // Navigation drawer, multi-column
            ExpandedLayout()
        }
    }
}
```

### Canonical Layouts

| Layout | Use Case | Window Class |
|--------|----------|--------------|
| **List-Detail** | Email, messages | Medium, Expanded |
| **Feed** | Social, news | All |
| **Supporting Pane** | Reference content | Medium, Expanded |

---

## 5. Android Navigation Patterns

### Navigation Components

| Component | Use Case | Position |
|-----------|----------|----------|
| **Bottom Navigation** | 3-5 top-level destinations | Bottom |
| **Navigation Rail** | Tablets, foldables | Left side, vertical |
| **Navigation Drawer** | Many destinations, large screens | Left side, hidden/visible |
| **Top App Bar** | Current context, actions | Top |

### Jetpack Navigation + Compose

> ✏️ ADDED: Navigation Compose was completely absent from the original.

```kotlin
// Dependency: androidx.navigation:navigation-compose
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            DetailScreen(id, navController)
        }
    }
}

// Navigate
navController.navigate("detail/123")

// Back
navController.popBackStack()
```

### Bottom Navigation

```
┌─────────────────────────────────────┐
│                                     │
│         Content Area                │
│                                     │
├─────────────────────────────────────┤
│  🏠     🔍     ➕     ❤️     👤    │ ← 80dp height
│ Home   Search  FAB   Saved  Profile│
└─────────────────────────────────────┘

Rules:
├── 3-5 destinations
├── Icons: Material Symbols (24dp)
├── Labels: Always visible (accessibility)
├── Active: Filled icon + indicator pill
├── Badge: For notifications
├── FAB can integrate (optional)
```

### Top App Bar

```
Types:
├── Center-aligned: Logo apps, simple
├── Small: Compact, scrolls away
├── Medium: Title + actions, collapses
├── Large: Display title, collapses to small

┌─────────────────────────────────────┐
│  ☰   App Title              🔔 ⋮  │ ← 64dp (small)
├─────────────────────────────────────┤
│                                     │
│         Content Area                │
└─────────────────────────────────────┘

Actions: Max 3 icons, overflow menu ( ⋮ ) for more
```

### Navigation Rail (Tablets)

```
┌───────┬─────────────────────────────┐
│  ≡    │                             │
│       │                             │
│  🏠   │                             │
│ Home  │       Content Area          │
│       │                             │
│  🔍   │                             │
│Search │                             │
│       │                             │
│  👤   │                             │
│Profile│                             │
└───────┴─────────────────────────────┘

Width: 80dp
Icons: 24dp
Labels: Below icon
FAB: Can be at top
```

### Back Navigation & Predictive Back

> ✏️ ADDED: Predictive back implementation — it was only mentioned in the checklist before.

```
Android provides system back:
├── Back button (3-button nav)
├── Back gesture (swipe from edge)
├── Predictive back (Android 14+)

Your app must:
├── Handle back correctly (pop stack)
├── Support predictive back animation
├── Never hijack/override back unexpectedly
└── Confirm before discarding unsaved work
```

**Predictive back in Compose (Android 14+, API 34):**

```kotlin
// Enable in AndroidManifest.xml:
// android:enableOnBackInvokedCallback="true"

// In your composable — handle back with animation
@Composable
fun ScreenWithPredictiveBack(onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
}

// For custom transitions, use SeekableTransitionState
@Composable
fun AnimatedBackScreen() {
    val transitionState = remember { SeekableTransitionState(false) }
    val scope = rememberCoroutineScope()

    PredictiveBackHandler { progress ->
        // Called with progress 0.0 → 1.0 as user swipes
        progress.collect { backEvent ->
            transitionState.seekTo(backEvent.progress)
        }
        // Back committed — snap to end
        scope.launch { transitionState.animateTo(true) }
    }
}
```

---

## 6. Material Components

### Buttons

```
Button Types:

┌──────────────────────┐
│    Filled Button     │  ← Primary action
└──────────────────────┘

┌──────────────────────┐
│    Tonal Button      │  ← Secondary, less emphasis
└──────────────────────┘

┌──────────────────────┐
│   Outlined Button    │  ← Tertiary, lower emphasis
└──────────────────────┘

    Text Button           ← Lowest emphasis

Heights:
├── Small: 40dp (when constrained)
├── Standard: 40dp
├── Large: 56dp (FAB size when needed)

Min touch target: 48dp (even if visual is smaller)
```

### Floating Action Button (FAB)

```
FAB Types:
├── Standard: 56dp diameter
├── Small: 40dp diameter
├── Large: 96dp diameter
├── Extended: Icon + text, variable width

Position: Bottom right, 16dp from edges
Elevation: Floats above content

┌─────────────────────────────────────┐
│                                     │
│         Content                     │
│                                     │
│                              ┌────┐ │
│                              │ ➕ │ │ ← FAB
│                              └────┘ │
├─────────────────────────────────────┤
│       Bottom Navigation             │
└─────────────────────────────────────┘
```

### Cards

```
Card Types:
├── Elevated: Shadow, resting state
├── Filled: Background color, no shadow
├── Outlined: Border, no shadow

Card Anatomy:
┌─────────────────────────────────────┐
│           Header Image              │ ← Optional
├─────────────────────────────────────┤
│  Title / Headline                   │
│  Subhead / Supporting text          │
├─────────────────────────────────────┤
│      [ Action ]    [ Action ]       │ ← Optional actions
└─────────────────────────────────────┘

Corner radius: 12dp (M3 default)
Padding: 16dp
```

### Text Fields

```
Types:
├── Filled: Background fill, underline
├── Outlined: Border all around

┌─────────────────────────────────────┐
│  Label                              │ ← Floats up on focus
│  ________________________________________________
│  │     Input text here...          │ ← Leading/trailing icons
│  ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
│  Supporting text or error           │
└─────────────────────────────────────┘

Height: 56dp
Label: Animates from placeholder to top
Error: Red color + icon + message
```

### Chips

```
Types:
├── Assist: Smart actions (directions, call)
├── Filter: Toggle filters
├── Input: Represent entities (tags, contacts)
├── Suggestion: Dynamic recommendations

┌───────────────┐
│  🏷️ Filter   │  ← 32dp height, 8dp corner radius
└───────────────┘

States: Unselected, Selected, Disabled
```

---

## 7. Android-Specific Patterns

### Snackbars

```
Position: Bottom, above navigation
Duration: 4-10 seconds
Action: One optional text action

┌─────────────────────────────────────────────────┐
│  Archived 1 item                    [ UNDO ]    │
└─────────────────────────────────────────────────┘

Rules:
├── Brief message, single line if possible
├── Max 2 lines
├── One action (text, not icon)
├── Can be dismissed by swipe
└── Don't stack, queue them
```

### Bottom Sheets

```
Types:
├── Standard: Interactive content
├── Modal: Blocks background (with scrim)

Modal Bottom Sheet:
┌─────────────────────────────────────┐
│                                     │
│        (Scrim over content)         │
│                                     │
├═════════════════════════════════════┤
│  ─────  (Drag handle, optional)     │
│                                     │
│        Sheet Content                │
│                                     │
│        Actions / Options            │
│                                     │
└─────────────────────────────────────┘

Corner radius: 28dp (top corners)
```

### Dialogs

```
Types:
├── Basic: Title + content + actions
├── Full-screen: Complex editing (mobile)
├── Date/Time picker
├── Confirmation dialog

┌─────────────────────────────────────┐
│              Title                  │
│                                     │
│       Supporting text that          │
│       explains the dialog           │
│                                     │
│           [ Cancel ]  [ Confirm ]   │
└─────────────────────────────────────┘

Rules:
├── Centered on screen
├── Scrim behind (dim background)
├── Max 2 actions aligned right
├── Destructive action can be on left
```

### Pull to Refresh

> ✏️ CHANGED: Replaced SwipeRefreshLayout (View system, deprecated for new projects)
> with the Compose Material3 PullToRefresh API.

**Compose (Material3) — recommended:**

```kotlin
// Dependency: androidx.compose.material3:material3 (1.2.0+)
@Composable
fun MyRefreshableScreen() {
    val viewModel: MyViewModel = hiltViewModel()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        state = pullToRefreshState
    ) {
        LazyColumn {
            items(viewModel.items) { item ->
                ItemRow(item)
            }
        }
    }
}
```

```
Visual behavior:
┌─────────────────────────────────────┐
│         ○ (Circular indicator)      │ ← Pulls down with gesture
├─────────────────────────────────────┤
│                                     │
│         Content                     │
│                                     │
└─────────────────────────────────────┘

Indicator: Material circular progress
Position: Top center, animates in on threshold
```

### Ripple Effect

```
Every touchable element needs ripple:

Touch down → Ripple expands from touch point
Touch up → Ripple completes and fades

Color:
├── On light: Black at ~12% opacity
├── On dark: White at ~12% opacity
├── On colored: Appropriate contrast

This is MANDATORY for Android feel.
```

---

## 8. Material Symbols

### Setup

> ✏️ ADDED: Dependency and usage — the original had none.

**Dependency (build.gradle.kts):**

```kotlin
implementation("androidx.compose.material:material-icons-extended")
// Or for the full Material Symbols variable font:
implementation("com.google.android.material:material:1.12.0")
```

**Compose usage:**

```kotlin
// Icons from the extended icon pack
Icon(
    imageVector = Icons.Outlined.Home,
    contentDescription = "Home",
    modifier = Modifier.size(24.dp)
)

// Filled variant (active/selected state)
Icon(
    imageVector = Icons.Filled.Home,
    contentDescription = "Home",
    tint = MaterialTheme.colorScheme.primary
)
```

### Usage Guidelines

```
Material Symbols: Google's icon library

Styles:
├── Outlined: Default, most common
├── Rounded: Softer, friendly
├── Sharp: Angular, precise

Variable font axes:
├── FILL: 0 (outline) to 1 (filled)
├── wght: 100-700 (weight)
├── GRAD: -25 to 200 (emphasis)
├── opsz: 20, 24, 40, 48 (optical size)
```

### Icon Sizes

| Size | Usage |
|------|-------|
| 20dp | Dense UI, inline |
| 24dp | Standard (most common) |
| 40dp | Larger touch targets |
| 48dp | Emphasis, standalone |

### States

```
Icon States:
├── Default: Full opacity
├── Disabled: 38% opacity
├── Hover/Focus: Container highlight
├── Selected: Filled variant + tint

Active vs Inactive:
├── Inactive: Outlined
├── Active: Filled + indicator
```

---

## 9. Android Accessibility

### TalkBack Requirements

> ✏️ CHANGED: Removed React Native code block — this is an Android-only doc.
> Kept Compose semantics example only.

```
Every interactive element needs:
├── contentDescription (what it is)
├── Correct semantics (button, checkbox, etc.)
├── State announcements (selected, disabled)
└── Grouping where logical
```

**Jetpack Compose:**

```kotlin
// Basic semantic annotation
Icon(
    imageVector = Icons.Default.PlayArrow,
    contentDescription = "Play",
    modifier = Modifier.semantics {
        role = Role.Button
    }
)

// Merging semantics for grouped content
Row(
    modifier = Modifier.semantics(mergeDescendants = true) {}
) {
    Icon(Icons.Default.Star, contentDescription = null)
    Text("Favourite")
}

// Custom state
Checkbox(
    checked = isChecked,
    onCheckedChange = { ... },
    modifier = Modifier.semantics {
        stateDescription = if (isChecked) "Checked" else "Unchecked"
    }
)
```

### Touch Target Size

```
MANDATORY: 48dp × 48dp minimum

Even if visual element is smaller:
├── Icon: 24dp visual, 48dp touch area
├── Checkbox: 20dp visual, 48dp touch area
└── Add padding to reach 48dp

Spacing between targets: 8dp minimum
```

**Compose — enforce minimum touch target:**

```kotlin
Icon(
    imageVector = Icons.Default.Close,
    contentDescription = "Dismiss",
    modifier = Modifier
        .size(24.dp)
        .clickable { dismiss() }
        .minimumInteractiveComponentSize() // enforces 48dp touch target
)
```

### Font Scaling

```
Android supports font scaling:
├── 85% (smaller)
├── 100% (default)
├── 115%, 130%, 145%...
├── Up to 200% (largest)

RULE: Test your UI at 200% font scale.
Use sp units and avoid fixed heights on containers
that hold text — use wrapContentHeight() instead.
```

### Reduce Motion

```kotlin
// Compose — check motion preference
val reduceMotion = LocalReduceMotion.current

// Or check via system settings
val animatorScale = Settings.Global.getFloat(
    context.contentResolver,
    Settings.Global.ANIMATOR_DURATION_SCALE,
    1f
)
val reduceMotion = animatorScale == 0f

if (reduceMotion) {
    // Skip or reduce animations
}
```

---

## 10. Jetpack Compose Essentials

> ✏️ NEW SECTION — Jetpack Compose was entirely absent from v1.

### MaterialTheme

```kotlin
// Access theme tokens anywhere in the composition
Text(
    text = "Hello",
    style = MaterialTheme.typography.bodyLarge,
    color = MaterialTheme.colorScheme.onSurface
)

Box(
    modifier = Modifier
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(16.dp)
)
```

### State Hoisting

```
RULE: Hoist state to the lowest common ancestor that needs it.

Don't do this (state buried in leaf):
└── Screen
    └── MyTextField (owns value + onValueChange) ← isolated state

Do this (state hoisted):
└── Screen (owns value + onValueChange)
    └── MyTextField(value, onValueChange) ← stateless
```

```kotlin
// Stateless composable — easy to test, easy to preview
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(value = value, onValueChange = onValueChange, modifier = modifier)
}

// Stateful wrapper — used in production
@Composable
fun MyTextFieldWithState() {
    var text by remember { mutableStateOf("") }
    MyTextField(value = text, onValueChange = { text = it })
}
```

### Side Effects

```kotlin
// LaunchedEffect — run a coroutine when key changes
LaunchedEffect(userId) {
    viewModel.loadUser(userId)
}

// DisposableEffect — clean up resources
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> ... }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
}

// SideEffect — sync Compose state to non-Compose code
SideEffect {
    analyticsTracker.setCurrentScreen(screenName)
}
```

### LazyColumn / LazyRow

```kotlin
// RULE: Never use Column with a forEach for long lists.
// Always use LazyColumn — it recycles items like RecyclerView.

LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    item { HeaderSection() }

    items(
        items = posts,
        key = { post -> post.id } // REQUIRED: stable keys prevent recomposition bugs
    ) { post ->
        PostCard(post)
    }

    item { FooterSection() }
}
```

### Edge-to-Edge (Android 15+)

> Android 15 enforces edge-to-edge. Your layout must handle system bar insets
> or content will be clipped by the navigation bar.

```kotlin
// In Activity.onCreate — enable edge-to-edge
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge() // androidx.activity:activity-compose
    setContent {
        AppTheme {
            AppNavHost()
        }
    }
}

// In your root composable — consume insets
Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = { BottomNavBar() }
) { innerPadding ->
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    )
}

// For custom layouts, consume insets manually
Box(
    modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.systemBars)
)

// Bottom sheet — consume only the bottom inset
ModalBottomSheet(
    onDismissRequest = { ... },
    windowInsets = WindowInsets.navigationBars // avoids double-padding
) { ... }
```

### Compose Performance Rules

```
Recomposition happens when State changes. Keep it cheap:

├── Use key() for lists to preserve identity
├── Use remember {} to cache expensive computations
├── Use derivedStateOf when state is derived from other state
├── Use @Stable / @Immutable on data classes passed to composables
├── Hoist lambdas — define onClick outside composable body
│   to prevent recreation on every recomposition
└── Use Modifier.graphicsLayer for animations — avoids full recompose

ANTI-PATTERN:
// Creates a new lambda every recomposition
Button(onClick = { viewModel.doSomething(item.id) })

CORRECT:
val onClick = remember(item.id) { { viewModel.doSomething(item.id) } }
Button(onClick = onClick)
```

---

## 11. Android Checklist

### Before Every Android Screen

- [ ] Using Material 3 components
- [ ] Touch targets ≥ 48dp
- [ ] Ripple effect on all touchables
- [ ] Roboto or Material type scale
- [ ] Semantic colors (dynamic color support)
- [ ] Back navigation works correctly

### Before Android Release

- [ ] Dark theme tested
- [ ] Dynamic color tested (Android 12+, API 31+)
- [ ] All font sizes tested (200% scale)
- [ ] TalkBack tested
- [ ] Predictive back implemented (Android 14+, API 34+)
- [ ] Edge-to-edge display handled — WindowInsets consumed (Android 15+)
- [ ] Different screen sizes tested (phones, tablets, foldables)
- [ ] Navigation patterns match platform (back, gestures)
- [ ] LazyColumn key= set on all list items
- [ ] No hardcoded #121212 dark colors (use MD3 colorScheme tokens)

---

> **Remember:** Android users expect Material Design. Custom designs that ignore Material patterns feel foreign and broken. Use Material components as your foundation, customize thoughtfully.