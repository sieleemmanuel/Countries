package com.siele.countries

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.siele.countries.ui.theme.CountriesTheme

@Composable
fun CountryDetails(navController: NavController) {
    Scaffold(
        topBar = { DetailsTopBar(navController=navController)}) {paddingValues ->
        DetailsContent(paddingValues)
    }
}

@Composable
fun DetailsContent(paddingValues: PaddingValues, modifier: Modifier=Modifier) {
   Box(modifier = modifier
       .padding(paddingValues)
       .fillMaxSize()) {
       val scrollState = rememberScrollState()
       Column(modifier = modifier
           .fillMaxWidth()
           .padding(horizontal = 16.dp)
       ) {
           Box(modifier = modifier
               .fillMaxWidth()
               .heightIn(200.dp),
               contentAlignment = Alignment.Center
           ){
               Image(
               painter = painterResource(id = R.drawable.ic_launcher_background),
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
           CountryData(title = "Population:", value = "Value")
           CountryData(title = "Region:", value = "Value")
           CountryData(title = "Capital:", value = "Value")
           CountryData(title = "Motto:", value = "Value")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Official Language:", value = "Value")
           CountryData(title = "Ethic group:", value = "Value")
           CountryData(title = "Religion:", value = "Value")
           CountryData(title = "Government:", value = "Value")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Independence:", value = "Value")
           CountryData(title = "Area:", value = "467.63 km2")
           CountryData(title = "Currency:", value = "Euro")
           CountryData(title = "GDP:", value = "US$3.400 billion")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Time zone:", value = "UTC+01")
           CountryData(title = "Date format:", value = "+376")
           CountryData(title = "Dialing code:", value = "+254")
           CountryData(title = "Driving side:", value = "Right")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")
           Spacer(modifier = modifier.heightIn(20.dp))
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")
           CountryData(title = "Title:", value = "Value")

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
        .background(color = Color(0xFF667085))) {
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
fun DetailsTopBar(navController: NavController, modifier: Modifier=Modifier) {
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = "CountryName",
                textAlign = TextAlign.Center,
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
@Preview(
    showSystemUi = true
)
@Composable
fun DetailsPreview() {
    CountriesTheme(darkTheme = true) {
       CountryDetails(navController = rememberNavController())
    }
}