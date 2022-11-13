package com.siele.countries.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.siele.countries.R
import com.siele.countries.ui.theme.CountriesTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

@Composable
fun CountryDetails(navController: NavController, selectedCountry: String?) {
    Scaffold(
        topBar = { DetailsTopBar(
            navController=navController,
            title = selectedCountry!!)
        }) { paddingValues ->
        DetailsContent(paddingValues, selectedCountry=selectedCountry)
    }
}

@Composable
fun DetailsContent(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    selectedCountry: String?,
    viewModel: CountriesViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val country = viewModel.getCountry(selectedCountry!!)
    Log.d("Details", "country: $country ")
    Box(modifier = modifier
       .padding(paddingValues)
        ) {

       Column(modifier = modifier
           .fillMaxWidth()
           .padding(all = 16.dp)
           .verticalScroll(state = scrollState)
       ) {
           Box(modifier = modifier
               .fillMaxWidth()
               .heightIn(200.dp),
               contentAlignment = Alignment.Center
           ){
               val painter = rememberImagePainter(
                   data = country?.flags?.last(),
                   builder = {
                       placeholder(R.drawable.ic_placeholder)
                   }
               )
               Image(
               painter = painter,
               contentDescription = "country flag" ,
               modifier = modifier
                   .fillMaxWidth()
                   .heightIn(200.dp)
                   .clip(RoundedCornerShape(10.dp)),
               contentScale = ContentScale.FillBounds
           )
               Row(modifier = modifier
                   .fillMaxWidth()
                   .padding(horizontal = 16.dp),
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   NavigateButton(
                       onClick = {},
                       desc = "Navigate before",
                       icon = R.drawable.ic_navigate_before
                   )
                   NavigateButton(
                       onClick = {},
                       desc = "Navigate next",
                       icon = R.drawable.ic_navigate_next
                   )
               }
           }
           Spacer(modifier = modifier.heightIn(20.dp))
           country?.population?.let {populationFormatter( it )}?.let { CountryData(title = "Population:", value = it) }
           country?.region?.let { CountryData(title = "Region:", value = it) }
           CountryData(title = "Capital:", value = country?.capital?.first().toString())
           country?.name?.official?.let { CountryData(title = "Motto:", value = it) }
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Official Language:", value = "Language")
           CountryData(title = "Ethic group:", value = "Na")
           CountryData(title = "Religion:", value = "Na")
           country?.name?.official?.let { CountryData(title = "Government:", value = it) }
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Independence:", value = country?.independent.toString())
           CountryData(title = "Area:", value = country?.area.toString()+" km2")
           CountryData(title = "Currency:", value = "Currency xxx")
           CountryData(title = "GDP:", value = "GBB")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Time zone:", value = country?.timezones?.first().toString())
           CountryData(title = "Date format:", value = "dd/mm/yyyy")
           CountryData(title = "Dialing code:", value = "+123")
           CountryData(title = "Driving side:", value = "Right")
           Spacer(modifier = modifier.heightIn(20.dp))
           country?.subregion?.let { CountryData(title = "Subregion:", value = it) }
           country?.fifa?.let { CountryData(title = "Title:", value = it) }

       }
   }

}

@Composable
private fun CountryData(
    modifier: Modifier= Modifier,
    title: String,
    value: String
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(top = 10.dp),) {
        Text(text = title, fontWeight = FontWeight.Bold)
        Spacer(modifier = modifier.width(10.dp))
        Text(text = value, fontWeight = FontWeight.Normal)
    }
}

@Composable
private fun NavigateButton(onClick:()->Unit,
                           desc:String,
                           icon:Int,
                           modifier: Modifier=Modifier) {
    Box(modifier = modifier
        .clip(CircleShape)
        .background(color = MaterialTheme.colors.primaryVariant)) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = desc,
                tint = MaterialTheme.colors.onSurface
            )
        }
    }

}

@Composable
fun DetailsTopBar(navController: NavController, modifier: Modifier=Modifier, title: String) {
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = title.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack()}) {
             Icon(Icons.Default.ArrowBack, "Navigate up")
            }
        }
    )
}
fun populationFormatter(int: Int): String {
    val formatter = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    formatter.applyPattern("#,###,###,###")
    return formatter.format(int.toDouble())
}
    @Preview(
    showSystemUi = true
)
@Composable
fun DetailsPreview() {
    CountriesTheme(darkTheme = true) {
       CountryDetails(
           navController = rememberNavController(),
           selectedCountry = "Kenya"
       )
    }
}