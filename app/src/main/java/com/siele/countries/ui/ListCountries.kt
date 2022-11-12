package com.siele.countries.ui

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.siele.countries.R
import com.siele.countries.utils.Screen
import java.util.*

@Composable
fun ListCountries(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar() },
        modifier = modifier
            .fillMaxSize()
    ) { paddingValues ->
        ListCountriesContent(paddingValues = paddingValues, navController = navController)
    }

}

@Composable
fun TopBar(modifier: Modifier = Modifier,viewModel: CountriesViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val dataStore = viewModel.getDataStore(context)
    val darkModeActive = dataStore.getDayMode
        .collectAsState(initial = isSystemInDarkTheme()).value?:false
    val isDarkMode by remember{ mutableStateOf( darkModeActive)}
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.h1,
                color = if(darkModeActive){MaterialTheme.colors.onSurface}else{Color(0xFF000F24)}
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
                    if (darkModeActive){
                        viewModel.setDarkMode(isDarkMode,dataStore)
                    }else{
                        viewModel.setDarkMode(!isDarkMode,dataStore)
                    }
                }) {
                Box(modifier = modifier
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

@Composable
fun ListCountriesContent(paddingValues: PaddingValues, modifier: Modifier = Modifier, navController: NavController) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SearchBar(focusManager = focusManager)
        SortList()
        CountriesList(navController =navController )
    }
}

@Composable
fun SortList(modifier: Modifier= Modifier ) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SortButton(onClickAction = {}, text = "EN", icon = R.drawable.ic_language)
        SortButton(onClickAction = {}, text = "Filter", icon = R.drawable.ic_filter)

    }
}

@Composable
private fun SortButton(modifier: Modifier= Modifier, onClickAction:()->Unit, text:String, icon: Int) {
    Button(
        onClick = onClickAction ,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountriesList(
    modifier: Modifier=Modifier,
                  navController: NavController,
                  viewModel: CountriesViewModel = hiltViewModel()
) {
    val countries = viewModel.countries.value.sortedBy {
        it.name.common
    }
    Log.d("ListCountries", "CountriesList: $countries ")
    Log.d("ListCountries", "Loading: ${viewModel.isLoading.value} ")
    Log.d("ListCountries", "Error: ${viewModel.isError.value} ")
    Log.d("ListCountries", "NetworkError: ${viewModel.networkError.value}")
    val groupedCountries = countries.groupBy { it.name.common[0] }
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        if (viewModel.isLoading.value){
            CircularProgressIndicator(
                modifier = modifier.size(40.dp),
                color = Color(0xFF03DAC5),
            )
            Log.d("ListCountries", "isLoading: ${viewModel.isLoading.value} ")
        }else{
            Log.d("ListCountries", "Loaded: ${viewModel.isLoading.value} ")
            LazyColumn(
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                groupedCountries.forEach {( initial, countryInitial )->
                    stickyHeader {
                        Text(
                            modifier = modifier
                                .background(color = MaterialTheme.colors.surface)
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            text = initial.toString())
                    }
                    items(items = countryInitial, key = {
                        it.name.official
                    }){ country ->
                        Row(modifier = modifier
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
                                Text(text = country.capital?.first().toString()?:"null")
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
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
        }),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = modifier
                        .clickable {
                            searchQuery = ""
                            focusManager.clearFocus()
                            /*onSearch(searchQuery, quotesViewModel = quotesViewModel)
                            quotesViewModel.emptySearch.value = false*/
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


/*@Preview(
    showSystemUi = true
)
@Composable
fun ListPreview() {
    CountriesTheme(darkTheme = true) {
        ListCountries(navController = rememberNavController())
    }
}*/
