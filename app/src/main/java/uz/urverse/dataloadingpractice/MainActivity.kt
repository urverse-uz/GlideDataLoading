package uz.urverse.dataloadingpractice

import MainViewModel
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uz.urverse.dataloadingpractice.adapter.MainAdapter
import uz.urverse.dataloadingpractice.databinding.ActivityMainBinding
import uz.urverse.dataloadingpractice.utils.PaginationScrollListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val viewModel: MainViewModel by viewModels()

    private var isLoading = false
    private var isLastPage = false
    private val totalPageCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)
        adapter = MainAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter

        viewModel.data.observe(this, { response ->
            adapter.addItems(response)
        })

        viewModel.fetchData()

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                adapter.addLoadingView()

                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.fetchMoreData()
                    adapter.removeLoadingView()
                    isLoading = false
                }, 1500)
            }

            override fun getTotalPageCount(): Int = totalPageCount
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
        })
    }
}