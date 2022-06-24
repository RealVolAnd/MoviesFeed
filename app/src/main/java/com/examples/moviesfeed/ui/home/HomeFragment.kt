package com.examples.moviesfeed.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examples.moviesfeed.App
import com.examples.moviesfeed.R
import com.examples.moviesfeed.databinding.HomeFragmentBinding
import com.examples.moviesfeed.model.Movie
import com.examples.moviesfeed.ui.home.adapters.MoviesAdapter
import com.examples.moviesfeed.utils.MsgUtils
import com.examples.moviesfeed.viewmodels.AppState
import com.examples.moviesfeed.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()
    private var _vb: HomeFragmentBinding? = null
    private val vb get() = _vb!!

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var msgUtils: MsgUtils


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
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        lifecycle.addObserver(viewModel)
        checkFirstLoad()
    }

    private fun checkFirstLoad() {
        if (App.firstList.isEmpty()) {
            viewModel.getMovieList(currentOffset)
        } else {
            setData(App.firstList)
        }
    }

    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                vb.loadingLayout.visibility = View.GONE
                setData(movieData)
            }
            is AppState.Loading -> {
                vb.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                vb.loadingLayout.visibility = View.GONE
                msgUtils.showToast(getString(R.string.no_movies_available))
            }
        }
    }

    private fun setData(movieData: List<Movie>) {
        fullMovieList.addAll(movieData)
        currentOffset += 20
        moviesAdapter.updateMovies(fullMovieList)
    }
}