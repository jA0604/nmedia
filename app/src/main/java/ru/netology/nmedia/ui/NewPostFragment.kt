package ru.netology.nmedia.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.AndroidUtils.hideKeyboard
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    private lateinit var npfBinding: FragmentNewPostBinding
    private val npfViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        npfBinding = FragmentNewPostBinding.inflate(inflater, container, false)
        return npfBinding.root
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        initialization()
    }

    private fun initialization() {

        npfBinding.edContentPost.setText(npfViewModel.contentEdit.value?.content)
        npfBinding.tvAuthor.setText(npfViewModel.contentEdit.value?.author)
        npfBinding.tvPostDate.setText(npfViewModel.contentEdit.value?.datePublished.toString()) //salfksdalkjlkjl


        npfBinding.edContentPost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                npfViewModel.changeContent(editable.toString())
            }

        })


        npfBinding.fabSave.setOnClickListener {
            if (npfBinding.edContentPost.text.isBlank()) {
                Toast.makeText(context, getString(R.string.post_not_empty), Toast.LENGTH_SHORT).show()
            } else {
                val content = npfBinding.edContentPost.text.toString()
                npfViewModel.changeContent(content)
                npfViewModel.save()
                it.hideKeyboard()
            }
        }

        npfViewModel.postCreated.observe(viewLifecycleOwner) {
            npfViewModel.loadPosts()
            findNavController().popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_item, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                val id = npfViewModel.contentEdit.value?.id ?: 0L
                if (id != 0L) npfViewModel.removeById(id)
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}