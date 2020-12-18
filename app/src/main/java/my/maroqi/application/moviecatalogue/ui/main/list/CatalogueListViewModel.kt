package my.maroqi.application.moviecatalogue.ui.main.list

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import my.maroqi.application.moviecatalogue.data.model.MovieData
import my.maroqi.application.moviecatalogue.data.model.MovieItem
import my.maroqi.application.moviecatalogue.data.model.TVData
import my.maroqi.application.moviecatalogue.data.model.TVItem
import my.maroqi.application.moviecatalogue.utility.ListItemType

class CatalogueListViewModel(svd: SavedStateHandle) : ViewModel() {

    val savedState = svd

    private val dataList = MutableLiveData<ArrayList<*>>()
    private var _dataList: ArrayList<*>? = null

    private lateinit var type: ListItemType
    private var vmCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    companion object {
        const val MOVIE_SVD = "movie_list_svd"
        const val TV_SVD = "tv_list_svd"
    }

    fun setType(type: ListItemType) {
        this.type = type

        if (this.type == ListItemType.MOVIE)
            getMovieListData()
        else if (this.type == ListItemType.TV_SHOW)
            getTVListData()
    }

    fun getDataList(): LiveData<ArrayList<*>> {
        return dataList
    }

    private fun getMovieListData() {
        vmCoroutineScope.launch {
            val movieListData: ArrayList<MovieItem> = arrayListOf()

            val titles = MovieData.titles
            val years = MovieData.years
            val releaseDate = MovieData.releaseDate
            val countries = MovieData.country
            val genres = MovieData.genres
            val durations = MovieData.duration
            val rating = MovieData.userScore
            val descs = MovieData.shortDesc
            val teams = MovieData.teams
            val actors = MovieData.actors

            titles.forEachIndexed { i, _ ->
                val movieItem = MovieItem(
                    title = titles[i],
                    year = years[i],
                    poster = i,
                    releaseDate = releaseDate[i],
                    country = countries[i],
                    genre = genres[i],
                    duration = durations[i],
                    rating = rating[i],
                    desc = descs[i],
                    teams = teams[i],
                    actors = actors[i]
                )

                movieListData.add(movieItem)
            }

            dataList.postValue(movieListData)
            _dataList = movieListData
            saveDataList()
        }
    }

    private fun getTVListData() {
        vmCoroutineScope.launch {
            val tvListData: ArrayList<TVItem> = arrayListOf()

            val titles = TVData.titles
            val years = TVData.years
            val releaseDate = TVData.releaseDate
            val genres = TVData.genres
            val durations = TVData.duration
            val rating = TVData.userScore
            val descs = TVData.shortDesc
            val teams = TVData.teams
            val actors = TVData.actors

            titles.forEachIndexed { i, _ ->
                val tvItem = TVItem(
                    title = titles[i],
                    year = years[i],
                    poster = i,
                    releaseDate = releaseDate[i],
                    genre = genres[i],
                    duration = durations[i],
                    rating = rating[i],
                    desc = descs[i],
                    teams = teams[i],
                    actors = actors[i]
                )

                tvListData.add(tvItem)
            }

            dataList.postValue(tvListData)
            _dataList = tvListData
            saveDataList()
        }
    }

    private fun saveDataList() {
        if (type == ListItemType.MOVIE) {
            if (savedState.contains(MOVIE_SVD)) {
                savedState.remove<ArrayList<*>>(MOVIE_SVD)
                savedState.set(MOVIE_SVD, _dataList)
            } else {
                savedState.set(MOVIE_SVD, _dataList)
            }
        } else if (type == ListItemType.TV_SHOW) {
            if (savedState.contains(TV_SVD)) {
                savedState.remove<ArrayList<*>>(TV_SVD)
                savedState.set(TV_SVD, _dataList)
            } else {
                savedState.set(TV_SVD, _dataList)
            }
        }
    }

    fun loadDataList(): ArrayList<*>? {
        return if (type == ListItemType.MOVIE) {
            savedState.get<ArrayList<*>>(MOVIE_SVD)
        } else {
            savedState.get<ArrayList<*>>(TV_SVD)
        }
    }
}