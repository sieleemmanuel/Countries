package com.siele.countries.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.siele.countries.R
import com.siele.countries.model.FilterItem
import com.siele.countries.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun ListCountries(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
//    Scaffold(
//        topBar = { TopBar() },
//        modifier = modifier
//            .fillMaxSize()
//    ) { paddingValues ->
//        ListCountriesContent(paddingValues = paddingValues, navController = navController)
//    }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    val searchQuery = rememberSaveable { mutableStateOf("") }

    BottomSheet(
        topBar = { TopBar() },
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetState = bottomSheetState,
        coroutineScope = coroutineScope

    ) { paddingValues ->
        ListCountriesContent(
            paddingValues = paddingValues,
            navController = navController,
            coroutineScope = coroutineScope,
            bottomSheetState = bottomSheetState,
            searchQuery = searchQuery,
        )
    }

}

@Composable
fun TopBar(modifier: Modifier = Modifier, viewModel: CountriesViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val dataStore = viewModel.getDataStore(context)
    val darkModeActive = dataStore.getDayMode
        .collectAsState(initial = isSystemInDarkTheme()).value ?: false
    val isDarkMode by remember { mutableStateOf(darkModeActive) }
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.h1,
                color = if (darkModeActive) {
                    MaterialTheme.colors.onSurface
                } else {
                    Color(0xFF000F24)
                }
            )
            Box(
                modifier = modifier
                    .padding(top = 10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF6F00))
                    .size(5.dp)

            )
        },
        actions = {
            IconToggleButton(
                checked = isDarkMode,
                onCheckedChange = {
                    if (darkModeActive) {
                        viewModel.setDarkMode(isDarkMode, dataStore)
                    } else {
                        viewModel.setDarkMode(!isDarkMode, dataStore)
                    }
                }) {
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colors.primaryVariant)
                ) {
                    Icon(
                        modifier = modifier.padding(5.dp),
                        painter = if (darkModeActive) {
                            painterResource(id = R.drawable.ic_dark_mode)
                        } else {
                            painterResource(id = R.drawable.ic_light_mode)
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
fun ListCountriesContent(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    navController: NavController,
    coroutineScope: CoroutineScope,
    bottomSheetState: BottomSheetState,
    searchQuery: MutableState<String>
) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SearchBar(focusManager = focusManager, searchQuery = searchQuery)
        SortList(coroutineScope = coroutineScope, bottomSheetState = bottomSheetState)
        CountriesList(
            navController = navController,
            searchQuery = searchQuery,
        )


    }
}

@ExperimentalMaterialApi
@Composable
fun SortList(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    bottomSheetState: BottomSheetState,
    countriesViewModel: CountriesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SortButton(onClickAction = {
            countriesViewModel.isFilterSheet.value = false
            coroutineScope.launch {
                bottomSheetState.expand()
            }
        }, text = "EN", icon = R.drawable.ic_language)
        SortButton(
            onClickAction = {
                countriesViewModel.isFilterSheet.value = true
                coroutineScope.launch {
                    bottomSheetState.expand()
                }

            },
            text = "Filter",
            icon = R.drawable.ic_filter
        )

    }
}

@Composable
private fun SortButton(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    text: String,
    icon: Int
) {
    Button(
        onClick = onClickAction,
        modifier = modifier
            .background(color = Color.Transparent)
            .border(
                width = 0.2.dp,
                shape = RoundedCornerShape(5.dp),
                color = MaterialTheme.colors.onSurface
            ),
        elevation = ButtonDefaults.elevation(0.dp),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = modifier.size(24.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = modifier.width(10.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }

    }
}

@ExperimentalFoundationApi
@Composable
fun CountriesList(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CountriesViewModel = hiltViewModel(),
    searchQuery: MutableState<String>,
) {
    val countries = when {
        viewModel.searchActive.value -> {
            viewModel.allCountries.value.filter {
                it.name.common.lowercase().contains(searchQuery.value)
            }
        }
        viewModel.filterActive.value -> {
            Log.d("CountriesFiltered", "Countries: ${viewModel.filteredCountries.value} ")
            viewModel.filteredCountries.value

        }
        else -> {
            viewModel.allCountries.value
        }
    }
   val groupedCountries = countries.groupBy { it.name.common[0] }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            viewModel.isLoading.value -> {
                CircularProgressIndicator(
                    modifier = modifier.size(40.dp),
                    color = Color(0xFF03DAC5),
                )
                Log.d("ListCountries", "isLoading: ${viewModel.isLoading.value} ")
            }
            viewModel.isServerError.value ->{
                Text(text = viewModel.serverErrorMessage.value)
                Spacer(modifier = modifier.heightIn(10.dp))
                Button(onClick = { viewModel.getCountries()}) {
                    Text(text = "Retry")
                }
            }
            viewModel.isNetworkError.value ->{
                Text(text = viewModel.networkErrorMessage.value)
                Spacer(modifier = modifier.heightIn(10.dp))
                Button(onClick = { viewModel.getCountries()}) {
                    Text(text = "Retry")
                }
            }
            else -> {
                Log.d("ListCountries", "Loaded: ${viewModel.isLoading.value} ")
                LazyColumn(
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier.fillMaxSize()
                ) {

                    groupedCountries.forEach { (initial, countryInitial) ->
                        stickyHeader {
                            Text(
                                modifier = modifier
                                    .background(color = MaterialTheme.colors.surface)
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                text = initial.toString()
                            )
                        }
                        items(items = countryInitial, key = {
                            it.name.official
                        }) { country ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        Log.d("List", "CountriesList: $country")
                                        navController.navigate(route = Screen.DetailsScreen.route + "/${country.name.common}")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val painter = rememberImagePainter(
                                    data = country.flags?.last(),
                                    builder = {
                                        placeholder(R.drawable.ic_placeholder)
                                    })
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier = modifier
                                        .size(50.dp)
                                        .clip(shape = RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.FillBounds
                                )
                                Spacer(modifier = modifier.width(20.dp))
                                Column {
                                    Text(text = country.name.common, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = modifier.heightIn(10.dp))
                                    Text(text = country.capital?.first().toString() ?: "null")
                                }

                            }
                        }

                    }

                }
            }
        }
    }

}

@Composable
fun SearchBar(
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    searchQuery: MutableState<String>,
    viewModel: CountriesViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }


    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = {
            searchQuery.value = it
            viewModel.searchActive.value = searchQuery.value.isNotEmpty()
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
        }),
        trailingIcon = {
            if (searchQuery.value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = modifier
                        .clickable {
                            searchQuery.value = ""
                            focusManager.clearFocus()
                        }
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = modifier.clickable {
                },
                tint = MaterialTheme.colors.onSurface
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.onSurface
        ),
        placeholder = {
            Text(
                text = "Search Country",
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(48.dp)
            .focusRequester(focusRequester = focusRequester)
            .background(MaterialTheme.colors.primary),
        shape = RoundedCornerShape(5.dp)
    )
}

@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    sheetState: BottomSheetState,
    coroutineScope: CoroutineScope,
    countriesViewModel: CountriesViewModel = hiltViewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    var continentsExpanded by rememberSaveable { mutableStateOf(false) }
    var timeZoneExpanded by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    BottomSheetScaffold(
        topBar = topBar,
        modifier = modifier,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
        sheetContent = {
            if (countriesViewModel.isFilterSheet.value) {
                Column(
                    modifier = modifier
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Box(
                            modifier = modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = MaterialTheme.colors.primaryVariant)
                                .clickable {
                                    coroutineScope.launch {
                                        sheetState.collapse()
                                    }
                                }

                        ) {
                            Icon(
                                modifier = modifier.padding(5.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = modifier.heightIn(10.dp))
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Continents",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            IconToggleButton(
                                checked = continentsExpanded,
                                onCheckedChange = {
                                    continentsExpanded = it
                                },
                                modifier = modifier
                            ) {
                                Icon(
                                    modifier = modifier.padding(5.dp),
                                    painter = if (continentsExpanded) {
                                        painterResource(id = R.drawable.ic_arrow_up)
                                    } else {
                                        painterResource(id = R.drawable.ic_arrow_down)
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                        Spacer(modifier = modifier.heightIn(10.dp))
                        if (continentsExpanded) {
                            FilterList(filters = countriesViewModel.continentsFilters)
                        }
                        Spacer(modifier = modifier.heightIn(10.dp))
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Time Zone",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            IconToggleButton(
                                checked = timeZoneExpanded,
                                onCheckedChange = {
                                    timeZoneExpanded = it
                                },
                                modifier = modifier
                            ) {
                                Icon(
                                    modifier = modifier.padding(5.dp),
                                    painter = if (timeZoneExpanded) {
                                        painterResource(id = R.drawable.ic_arrow_up)
                                    } else {
                                        painterResource(id = R.drawable.ic_arrow_down)
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        }

                        Spacer(modifier = modifier.heightIn(10.dp))
                        if (timeZoneExpanded) {
                            FilterList(filters = countriesViewModel.timeZonesFilters)
                        }
                        Spacer(modifier = modifier.heightIn(10.dp))
                            Row(modifier = modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    modifier = modifier
                                        .heightIn(48.dp),
                                    border = BorderStroke(
                                        color = MaterialTheme.colors.onSurface,
                                        width = 2.dp
                                    ),
                                    onClick = {
                                        countriesViewModel.filterActive.value = false
                                        countriesViewModel.searchActive.value = false
                                        countriesViewModel.selectedFilters.value.clear()
                                        countriesViewModel.resetFilters()
                                        coroutineScope.launch {
                                            sheetState.collapse()
                                        }

                                    }) {
                                    Text(
                                        text = "Reset", color = MaterialTheme.colors.onSurface
                                    )
                                }
                                Spacer(modifier = modifier.width(30.dp))
                                Button(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .heightIn(48.dp),
                                    onClick = {
                                        if (countriesViewModel.selectedFilters.value.isNotEmpty()) {
                                            val filtered =
                                                countriesViewModel.allCountries.value.filter {
                                                    countriesViewModel.stringFilters.value.contains(
                                                        it.region
                                                    ) || countriesViewModel.stringFilters.value.contains(
                                                        it.timezones?.first()
                                                    )
                                                }
                                            countriesViewModel.filteredCountries.value = filtered
                                            countriesViewModel.filterActive.value = true
                                            countriesViewModel.searchActive.value = false
                                        }
                                        coroutineScope.launch {
                                            sheetState.collapse()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFFF6F00)
                                    )
                                ) {
                                    Text(
                                        text = "Show results",
                                        color = Color.White
                                    )
                                }
                            }
                    }
                }
            }else{
                Column(
                    modifier = modifier
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Languages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Box(
                            modifier = modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = MaterialTheme.colors.primaryVariant)
                                .clickable {
                                    coroutineScope.launch {
                                        sheetState.collapse()
                                    }
                                }

                        ) {
                            Icon(
                                modifier = modifier.padding(5.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    Spacer(modifier = modifier.heightIn(10.dp))
                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                     val (selectedOption, onOptionSelected) = remember {
                         mutableStateOf(countriesViewModel.languages[2])
                     }
                        countriesViewModel.languages.forEach {  lang ->
                            Row(modifier = modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (lang == selectedOption),
                                    onClick = {
                                        onOptionSelected(lang)
                                    }
                                ),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = lang)
                                RadioButton(
                                    selected = (lang == selectedOption),
                                    onClick = { onOptionSelected(lang)})
                            }

                        }

                    }}
            }
        },
        scaffoldState = bottomSheetScaffoldState
    ) { paddingValue ->
        content(paddingValue)
    }


}

@Composable
private fun FilterList(
    modifier: Modifier = Modifier,
    filters: MutableState<List<FilterItem>>,
    countriesViewModel: CountriesViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp)
    ) {
        items(filters.value) { filterItem ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = filterItem.filter)
                Checkbox(checked = filterItem.selected.value,
                    onCheckedChange = {
                        filterItem.selected.value = it
                        if (filterItem.selected.value) {
                            if (!countriesViewModel.selectedFilters.value.contains(filterItem)) {
                                countriesViewModel.selectedFilters.value.add(filterItem)
                                countriesViewModel.stringFilters.value =
                                    countriesViewModel.selectedFilters.value.map { filterItem -> filterItem.filter }
                                        .toMutableList()
                            }
                            Log.d("Filters", "Filters::${countriesViewModel.stringFilters.value} ")
                        } else {
                            countriesViewModel.selectedFilters.value.remove(filterItem)
                            countriesViewModel.stringFilters.value =
                                countriesViewModel.selectedFilters.value.map { filterItem -> filterItem.filter }
                                    .toMutableList()

                            Log.d("Filters", "Filters::${countriesViewModel.stringFilters.value} ")
                        }
                    })
            }
        }
    }
}
