package com.examples.moviesfeed.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examples.moviesfeed.R
import com.examples.moviesfeed.databinding.HomeFragmentBinding
import com.examples.moviesfeed.model.Movie
import com.examples.moviesfeed.ui.home.adapters.MoviesAdapter
import com.examples.moviesfeed.viewmodels.AppState
import com.examples.moviesfeed.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _vb: HomeFragmentBinding? = null
    private val vb get() = _vb!!

    @Inject lateinit var moviesAdapter : MoviesAdapter


    private var fullMovieList: ArrayList<Movie> = ArrayList()
    private var currentOffset: Int = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = HomeFragmentBinding.inflate(inflater, container, false)
        with(vb) {
            moviesList.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.getMovieList(currentOffset)
                    }
                }
            })
           // moviesAdapter = MoviesAdapter()
            moviesList.adapter = moviesAdapter
            moviesList.setItemViewCacheSize(20)
            return root
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        lifecycle.addObserver(viewModel)
        viewModel.getMovieList(currentOffset)
    }

    private fun renderData(appState: AppState) {
        val loadingLayout: FrameLayout? = view?.findViewById(R.id.loadingLayout)

        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                loadingLayout?.visibility = View.GONE
                setData(movieData)
            }
            is AppState.Loading -> {
                loadingLayout?.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout?.visibility = View.GONE
                Toast.makeText(context, R.string.no_movies_available, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setData(movieData: List<Movie>) {
        fullMovieList.addAll(movieData)
        currentOffset += 20
        moviesAdapter.updateMovies(fullMovieList)
    }
}