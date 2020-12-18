package my.maroqi.application.moviecatalogue.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import my.maroqi.application.moviecatalogue.data.model.MovieData
import my.maroqi.application.moviecatalogue.data.model.MovieItem
import my.maroqi.application.moviecatalogue.data.model.TVData
import my.maroqi.application.moviecatalogue.data.model.TVItem
import my.maroqi.application.moviecatalogue.utility.ListItemType

class DataDetailsViewModel(svd: SavedStateHandle) : ViewModel() {
    val savedState = svd

    private val detailMovie = MutableLiveData<MovieItem>()
    private var _detailMovie: MovieItem? = null

    private val detailTV = MutableLiveData<TVItem>()
    private var _detailTV: TVItem? = null

    private lateinit var type: ListItemType
    private var vmCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    companion object {
        const val MOVIE_D_SVD = "movie_detail_svd"
        const val TV_D_SVD = "tv_detail_svd"
    }

    fun setType(type: ListItemType, index: Int) {
        this.type = type

        if (this.type == ListItemType.MOVIE)
            getMovieDetail(index)
        else if (this.type == ListItemType.TV_SHOW)
            getTVDetail(index)
    }

    fun getMovieDetail(): LiveData<MovieItem> {
        return detailMovie
    }

    fun getTVDetail(): LiveData<TVItem> {
        return detailTV
    }

    private fun getMovieDetail(index: Int) {
        vmCoroutineScope.launch {
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

            val movieItem = MovieItem(
                title = titles[index],
                year = years[index],
                poster = index,
                releaseDate = releaseDate[index],
                country = countries[index],
                genre = genres[index],
                duration = durations[index],
                rating = rating[index],
                desc = descs[index],
                teams = teams[index],
                actors = actors[index]
            )

            detailMovie.postValue(movieItem)
            _detailMovie = movieItem
            saveDataDetail()
        }
    }

    private fun getTVDetail(index: Int) {
        vmCoroutineScope.launch {
            val titles = TVData.titles
            val years = TVData.years
            val releaseDate = TVData.releaseDate
            val genres = TVData.genres
            val durations = TVData.duration
            val rating = TVData.userScore
            val descs = TVData.shortDesc
            val teams = TVData.teams
            val actors = TVData.actors

            val tvItem = TVItem(
                title = titles[index],
                year = years[index],
                poster = index,
                releaseDate = releaseDate[index],
                genre = genres[index],
                duration = durations[index],
                rating = rating[index],
                desc = descs[index],
                teams = teams[index],
                actors = actors[index]
            )

            detailTV.postValue(tvItem)
            _detailTV = tvItem
            saveDataDetail()
        }
    }

    private fun saveDataDetail() {
        if (type == ListItemType.MOVIE) {
            if (savedState.contains(MOVIE_D_SVD)) {
                savedState.remove<MovieItem>(MOVIE_D_SVD)
                savedState.set(MOVIE_D_SVD, _detailMovie)
            } else {
                savedState.set(MOVIE_D_SVD, _detailMovie)
            }
        } else if (type == ListItemType.TV_SHOW) {
            if (savedState.contains(TV_D_SVD)) {
                savedState.remove<TVItem>(TV_D_SVD)
                savedState.set(TV_D_SVD, _detailTV)
            } else {
                savedState.set(TV_D_SVD, _detailTV)
            }
        }
    }

    fun loadDataDetail(): Any? {
        return if (type == ListItemType.MOVIE) {
            savedState.get<MovieItem>(MOVIE_D_SVD)
        } else {
            savedState.get<TVItem>(TV_D_SVD)
        }
    }
}