package com.example.sookplace.ui.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sookplace.R
import com.example.sookplace.data.remote.response.PostContent
import com.example.sookplace.databinding.CommunityRvItemBinding

class CommunityRVAdapter(
    private val onPlaceClick: (String) -> Unit, // 장소보기 클릭 리스너
    private val onItemClick: (PostContent) -> Unit,   // 게시글 상세 클릭 리스너
    private val onLikeClick: (PostContent) -> Unit    // 좋아요 클릭 리스너
) : ListAdapter<PostContent, CommunityRVAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: CommunityRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostContent) {
            //유저 정보
            binding.userName.text = post.author.nickname
            binding.time.text = post.displayTime
            binding.profileImg.load(post.author.profileImageUrl) {
                crossfade(true)
                placeholder(R.drawable.my_page_icon)
                error(R.drawable.my_page_icon)
                transformations(CircleCropTransformation()) // 프로필은 동그랗게
            }

            //게시글 본문
            binding.title.text = post.title
            binding.content.text = post.excerpt

            //메인 이미지 (있을 때만 노출)
            if (!post.imageUrl.isNullOrEmpty()) {
                binding.postImg.isVisible = true
                binding.postImg.load(post.imageUrl) {
                    crossfade(true)
//                    placeholder(R.drawable.loading_image) //이미지를 다운로드하는 동안 보여줄 이미지
//                    error(R.drawable.error_image)       //인터넷 연결 실패 등으로 로딩 실패 시 보여줄 이미지
                }
            } else {
                binding.postImg.isVisible = false
            }

            //장소 정보
            binding.placeName.text = post.place.name
            binding.placeBtn.setOnClickListener { onPlaceClick(post.place.placeId) }

            //하단 수치
            binding.heartCount.text = post.likeCount.toString()
            binding.commentCount.text = post.commentCount.toString()

            //좋아요 아이콘
            binding.heart.setImageResource(
                if (post.likedByMe) R.drawable.favorite_fill else R.drawable.favorite
            )

            binding.heart.setOnClickListener {
                onLikeClick(post) // 어댑터 생성 시 넘겨받은 고차함수 호출
            }

            //평점
            binding.ratingScore.text = post.rating.toString()
            val starCount = Math.round(post.rating).toInt()
            val stars = listOf(
                binding.star1, binding.star2, binding.star3, binding.star4, binding.star5
            )
            for (i in 0 until 5) {
                if (i < starCount) {
                    stars[i].setImageResource(R.drawable.star) // 채워진 별
                } else {
                    stars[i].setImageResource(R.drawable.star_outline) // 빈 별
                }
            }

            //게시글 클릭 리스너
            binding.root.setOnClickListener { onItemClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommunityRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostContent>() {
            override fun areItemsTheSame(oldItem: PostContent, newItem: PostContent): Boolean =
                oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: PostContent, newItem: PostContent): Boolean =
                oldItem == newItem
        }
    }

}