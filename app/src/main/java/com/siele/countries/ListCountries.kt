package com.siele.countries

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.siele.countries.ui.theme.CountriesTheme
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
        ListCountriesContent(paddingValues = paddingValues)
    }

}

@Composable
fun TopBar() {
    val isDarkMode by remember { mutableStateOf(false) }
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = "Explore",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconToggleButton(
                checked = isDarkMode,
                onCheckedChange = {

                }) {
                Icon(
                    painter = if (isDarkMode) {
                        painterResource(id= R.drawable.ic_dark_mode)
                    } else {
                        painterResource(id= R.drawable.ic_light_mode)
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )

            }
        }
    )
}

@Composable
fun ListCountriesContent(paddingValues: PaddingValues, modifier: Modifier = Modifier) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SearchBar(focusManager = focusManager)
        SortList()
        CountriesList()
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
fun CountriesList(modifier: Modifier=Modifier) {
    val countries = listOf("Kenya", "Andora", "Uganda", "Burundi", "Mali", "Ethiopia", "Gabon")
    val groupedCountries = countries.groupBy { it[0] }
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        groupedCountries.forEach {( initial, countryInitial )->
            stickyHeader {
                Text(
                    text = initial.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
            }
            items(countryInitial){
                Row(modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = modifier
                            .size(50.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = modifier.width(20.dp))
                    Column {
                        Text(text = "CountryName", fontWeight = FontWeight.Bold)
                        Spacer(modifier = modifier.heightIn(10.dp))
                        Text(text = "CapitalCity")
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
/*quotesViewModel: QuotesViewModel = hiltViewModel()*/
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
// onSearch(searchQuery, quotesViewModel = quotesViewModel)
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
                    // onSearch(searchQuery, quotesViewModel = quotesViewModel)
                },
                tint = MaterialTheme.colors.onSurface
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
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


@Preview(
    showSystemUi = true
)
@Composable
fun ListPreview() {
    CountriesTheme(darkTheme = true) {
        ListCountries(navController = rememberNavController())
    }
}
