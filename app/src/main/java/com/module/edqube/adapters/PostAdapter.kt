package com.module.edqube.adapters

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.module.edqube.R
import com.module.edqube.models.PostItem

class PostAdapter(
    private val context: Context,
    private val postList: List<PostItem>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val postContentContainer: FrameLayout = itemView.findViewById(R.id.postContentContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int = postList.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.postContentContainer.removeAllViews()

        when (post) {
            is PostItem.TextPost -> {
                Glide.with(context)
                    .load(post.avatar)
                    .placeholder(R.drawable.profile)
                    .into(holder.avatar)

                holder.userName.text = post.user
                val view = LayoutInflater.from(context).inflate(R.layout.item_post_text, holder.postContentContainer, false)
                view.findViewById<TextView>(R.id.textContent).text = post.content
                holder.postContentContainer.addView(view)
            }

            is PostItem.ImageTextPost -> {
                Glide.with(context)
                    .load(post.avatar)
                    .placeholder(R.drawable.profile)
                    .into(holder.avatar)
                holder.userName.text = post.user

                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_post_image, holder.postContentContainer, false)

                val description = view.findViewById<TextView>(R.id.txtDesc)
                val viewPager = view.findViewById<ViewPager2>(R.id.vpMedia)
                val dotsIndicator = view.findViewById<com.tbuonomo.viewpagerdotsindicator.DotsIndicator>(R.id.dots)

                description.text = post.content

                val adapter = MediaAdapter(context, post.imageUrls)
                viewPager.adapter = adapter
                dotsIndicator.setViewPager2(viewPager)

                // 🛠️ Disable outer ViewPager tab swipe while swiping images
                try {
                    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
                    recyclerViewField.isAccessible = true
                    val internalRecyclerView = recyclerViewField.get(viewPager) as RecyclerView

                    internalRecyclerView.setOnTouchListener { v, event ->
                        val direction = event.historySize.takeIf { it > 0 }?.let {
                            val dx = event.x - event.getHistoricalX(0)
                            val dy = event.y - event.getHistoricalY(0)
                            Pair(dx, dy)
                        } ?: Pair(0f, 0f)

                        val (dx, dy) = direction
                        val isHorizontalSwipe = kotlin.math.abs(dx) > kotlin.math.abs(dy)

                        if (isHorizontalSwipe) {
                            viewPager.parent?.requestDisallowInterceptTouchEvent(true)
                        } else {
                            viewPager.parent?.requestDisallowInterceptTouchEvent(false)
                        }
                        false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                holder.postContentContainer.addView(view)
            }


            is PostItem.PollPost -> {
                Glide.with(context)
                    .load(post.avatar)
                    .placeholder(R.drawable.profile)
                    .into(holder.avatar)
                holder.userName.text = post.user

                val view = LayoutInflater.from(context).inflate(R.layout.item_post_poll, holder.postContentContainer, false)
                val question = view.findViewById<TextView>(R.id.txtPollQuestion)
                val optionContainer = view.findViewById<LinearLayout>(R.id.optionsContainer)

                question.text = post.question
                optionContainer.removeAllViews()

                val totalVotes = post.voteCounts.sum().coerceAtLeast(1)

                post.options.forEachIndexed { index, optionText ->
                    val optionView = LayoutInflater.from(context)
                        .inflate(R.layout.item_poll_option_fill, optionContainer, false)

                    val fillContainer = optionView.findViewById<View>(R.id.fillBackground)
                    val label = optionView.findViewById<TextView>(R.id.txtOptionNumberAndLabel)
                    val percentText = optionView.findViewById<TextView>(R.id.txtPercentage)
                    val root = optionView.findViewById<FrameLayout>(R.id.pollOptionRoot)

                    val percent = (post.voteCounts[index] * 100f / totalVotes).toInt()
                    label.text = "${index + 1}. $optionText"
                    percentText.text = "$percent%"

                    if (post.userVotedIndex != null) {
                        // VOTED: animate fill
                        fillContainer.visibility = View.VISIBLE
                        percentText.visibility = View.VISIBLE
                        fillContainer.layoutParams.width = 0

                        fillContainer.post {
                            val fullWidth = root.width
                            val targetWidth = (fullWidth * percent / 100f).toInt()

                            val animator = ValueAnimator.ofInt(0, targetWidth)
                            animator.duration = 500
                            animator.addUpdateListener {
                                fillContainer.layoutParams.width = it.animatedValue as Int
                                fillContainer.requestLayout()
                            }
                            animator.start()
                        }
                    } else {
                        // NOT VOTED: click to vote
                        fillContainer.visibility = View.INVISIBLE
                        root.setOnClickListener {
                            post.voteCounts[index]++
                            post.userVotedIndex = index
                            notifyItemChanged(holder.adapterPosition)
                        }
                    }

                    optionContainer.addView(optionView)
                }

                holder.postContentContainer.removeAllViews()
                holder.postContentContainer.addView(view)
            }

        }
    }
}
