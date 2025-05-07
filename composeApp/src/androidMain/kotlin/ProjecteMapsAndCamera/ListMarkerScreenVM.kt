package ProjecteMapsAndCamera

import androidx.lifecycle.ViewModel
import cat.itb.m78.exercices.Exercicis.BDD.database

class ListMarkerScreenVM : ViewModel() {
    //Data base
    val mapsMarkerQueries = database.mapsQueries
    var all = mapsMarkerQueries.selectAll().executeAsList()
    fun insert(title: String, y: Double, x : Double, url: String){
        mapsMarkerQueries.insert(title, y, x, url)
    }
}