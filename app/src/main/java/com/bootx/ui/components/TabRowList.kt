package com.bootx.ui.components;

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TabRowList(tabs: List<String>,selectedTabIndex: Int,onClick:(selectTabIndex: Int)->Unit) {
    ScrollableTabRow(selectedTabIndex = selectedTabIndex,
        modifier =  Modifier.wrapContentWidth(),
        edgePadding = 18.dp,
        divider = @Composable {

        },
        tabs = {
            tabs.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    selected = selectedTabIndex == index,
                    onClick = {
                        onClick(index)
                    }) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    )
}



