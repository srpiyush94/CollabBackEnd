package org.collaborative.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.collaborative.model.*;
import org.collaborative.service.NotificationService;
import org.collaborative.service.PostCommentService;
import org.collaborative.service.PostLikesService;
import org.collaborative.service.PostService;
import org.collaborative.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

	@Autowired
	UserService userService;

	@Autowired
	PostService postService;

	@Autowired
	PostCommentService postCommentService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	PostLikesService postPostLikesService;

	private static Logger log = LoggerFactory.getLogger(PostController.class);

	@RequestMapping(value = "/createPost", method = RequestMethod.POST)
	public ResponseEntity<?> createPost(@RequestBody Post post, HttpSession session) {
		log.info("Create New Post: fetch user session details");

		User user = (User) session.getAttribute("validUser");
		// Integer userId = (Integer) session.getAttribute("userId");
		if (user == null) {
			log.info("Create New Post: user session details Not Found");
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			user = userService.getUserById(user.getId());
			log.info("Create New Post: user details fetched = " + user.toString());
			if (user.isEnabled()) {
				log.info("Create New Post: if user is online create new Post ");
				post.setStatus("PENDING");
				post.setCreatedDate(new Date());
				post.setPostedBy(user);
				post.setNoOfLikes(0);
				if (postService.addPost(post)) {
					return new ResponseEntity<Post>(post, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(new ErrorClass(11, "Post Creation failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(12, "User must be logged in to create Post"),
					HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/viewApprovedPosts", method = RequestMethod.GET)
	public ResponseEntity<?> viewApprovedPosts() {
		log.info("ViewApprovedPosts: fetch all Approved Posts details");
		List<Post> PostList = postService.getAllApprovedPost();
		if (PostList != null) {
			return new ResponseEntity<List<Post>>(PostList, HttpStatus.OK);
		} else {
			return new ResponseEntity<ErrorClass>(new ErrorClass(13, "Retrieving Post details failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/viewPostById/{PostId}", method = RequestMethod.GET)
	public ResponseEntity<?> viewPostById(@PathVariable("PostId") int PostId) {
		log.info("ViewPostById: fetch Post details by Id = " + PostId);
		Post post = postService.getPost(PostId);
		log.info("ViewPostById:  Post details by Id = " + post);
		if (post != null) {
			log.info("ViewPostById: Post details: " + post.getId() + " Date: " + post.getCreatedDate());
			return new ResponseEntity<Post>(post, HttpStatus.OK);
		} else {
			return new ResponseEntity<ErrorClass>(new ErrorClass(13, "Retrieving Post details failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/viewPendingPosts", method = RequestMethod.GET)
	public ResponseEntity<?> viewPendingPosts(HttpSession session) {
		log.info("ViewPendingPosts: fetch all pending Post details  ");
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			log.info("ViewPendingPosts: user session details not found  ");
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			List<Post> PostList = postService.getAllPendingPost();
			log.info("ViewPendingPosts: pending Post List :  " + PostList);
			if (PostList != null) {
				return new ResponseEntity<List<Post>>(PostList, HttpStatus.OK);
			} else {
				return new ResponseEntity<ErrorClass>(new ErrorClass(13, "Retrieving Post details failed"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@RequestMapping(value = "/viewAllPosts", method = RequestMethod.GET)
	public ResponseEntity<?> viewAllPosts() {
		log.info("View All Posts: fetch all Post details  ");
		List<Post> PostList = postService.getAllPost();

		log.info("View All Posts:  Post details List = " + PostList);
		if (PostList != null) {
			return new ResponseEntity<List<Post>>(PostList, HttpStatus.OK);
		} else {
			return new ResponseEntity<ErrorClass>(new ErrorClass(13, "Retrieving Post details failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/viewUserPosts", method = RequestMethod.GET)
	public ResponseEntity<?> viewUserPosts(HttpSession session) {
		log.info("View User Posts: fetch all current user Post details  ");
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			List<Post> PostList = postService.getAllPost(userId);
			log.info("View User Posts: fetch all current user Post details fetched =  " + PostList);
			if (PostList != null) {
				return new ResponseEntity<List<Post>>(PostList, HttpStatus.OK);
			} else {
				return new ResponseEntity<ErrorClass>(new ErrorClass(13, "Retrieving Post details failed"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@RequestMapping(value = "/approvePost/{PostId}", method = RequestMethod.GET)
	public ResponseEntity<?> approvePost(@PathVariable("PostId") int PostId, HttpSession session) {
		log.info("Approved Posts: Approve Post details  ");
		
		User user = (User) session.getAttribute("validUser");
		//Integer userId = (Integer) session.getAttribute("userId");
		if (user == null) {
	return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),HttpStatus.UNAUTHORIZED);
		} else {
			Post PostObj = postService.getPost(PostId);
			if (PostObj != null) {
				if (postService.approvePost(PostObj)) {
					PostObj = postService.getPost(PostId);
					log.info("Approved Posts: Approve Post details successful " + PostObj);
					// Add entry in Notification for the Post
					Notification notification = new Notification();
					notification.setNotificationType("Post");
					notification.setNotificationReferenceId(PostObj.getId());
					notification.setNotificationDesc(PostObj.getBlogTitle());
					notification.setApprovalStatus("APPROVED");
					notification.setViewed(false);
					notification.setUserId(user.getId().intValue());
					notificationService.addNotification(notification);
					log.info("Approved Posts:Add notification details " + notification);
					return new ResponseEntity<Post>(PostObj, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(
							new ErrorClass(14, "Error occured during approving Post details"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<ErrorClass>(new ErrorClass(15, "No Posts found for given Id"),HttpStatus.NOT_FOUND);
			}
		}
	}

	@RequestMapping(value = "/rejectPost/{PostId}", method = RequestMethod.GET)
	public ResponseEntity<?> rejectPost(@PathVariable("PostId") int PostId,@RequestParam(required = false) String rejectionReason, HttpSession session) {
		log.info("Reject Posts: Reject Post details  ");
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			Post post = postService.getPost(PostId);

			if (post != null) {

				if (postService.rejectPost(post)) {
					post = postService.getPost(PostId);

					Notification notification = new Notification();
					notification.setNotificationType("Post");
					notification.setNotificationReferenceId(post.getId());
					notification.setNotificationDesc(post.getBlogTitle());

					notification.setApprovalStatus("REJECTED");
					notification.setViewed(false);
					if (rejectionReason == null) {
						notification.setRejectionReason("Reason not mentioned by Admin");
					} else {
						notification.setRejectionReason(rejectionReason);
					}

					notificationService.addNotification(notification);

					return new ResponseEntity<Post>(post, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(
							new ErrorClass(14, "Error occured during rejecting Post details"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<ErrorClass>(new ErrorClass(15, "No Posts found for given Id"),
						HttpStatus.NOT_FOUND);
			}
		}
	}

	@RequestMapping(value = "/getPostComment/{PostId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPostComment(@PathVariable("PostId") int PostId) {
		log.info("GetPostComment: get Post comment by Id =  " + PostId);
		List<Comment> commentList = postCommentService.getAllPostComments(PostId);

		if (commentList != null) {

			return new ResponseEntity<List<Comment>>(commentList, HttpStatus.OK);
		} else {
			return new ResponseEntity<ErrorClass>(new ErrorClass(17, "Retrieving Post Comment details failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/addPostComment", method = RequestMethod.POST)
	public ResponseEntity<?> addPostComment(@RequestBody Comment comment, HttpSession session) {
		log.info("AddPostComment: Post comment Post Id= " + comment.getId());
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.getUserById(userId);
			if (user.isEnabled()) {
				comment.setCommentedOn(new Date());

				if (postCommentService.addPostComment(comment)) {
					return new ResponseEntity<Comment>(comment, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(new ErrorClass(19, "Post Comment Addition failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(12, "User must be logged in to add a comment"),
					HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/updateLikes", method = RequestMethod.POST)
	public ResponseEntity<?> updateNoOfLikes(@RequestBody Post post, HttpSession session) {
		log.info("Update No Of Likes ");
		//Integer userId = (Integer) session.getAttribute("userId");

		User user = (User) session.getAttribute("validUser");
		if (user.isEnabled()) {
			// if(PostService.updatePost(Post)){
			Post updatedPost = postPostLikesService.updateBlogPostLikes(post, user);

			if (updatedPost != null) {
				return new ResponseEntity<Post>(post, HttpStatus.OK);
			} else {
				return new ResponseEntity<ErrorClass>(new ErrorClass(11, "Post Likes updation failed"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<ErrorClass>(new ErrorClass(12, "User must be logged in to like"),
				HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/updatePost", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePost(@RequestBody Post post, HttpSession session) {
		log.info("Update Posts: Update Post details ");
		Integer userId = (Integer) session.getAttribute("userId");
		Notification notification;
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.getUserById(userId);

			if (user.isEnabled()) {
				Post tempPost = postService.getPost(post.getId());
				tempPost.setBlogTitle(post.getBlogTitle());
				tempPost.setBlogContent(post.getBlogContent());
				// if(tempPost.getStatus().equals("REJECTED")){
				tempPost.setStatus("PENDING");
				notification = notificationService.getNotification("Post", post.getId());

				if (notification != null) {
					System.out.println("notification = " + notification.toString());
					if (notificationService.deleteNotification(notification)) {
						System.out.println("Notification deleted successfully");
					}
				}
				// }
				if (postService.updatePost(tempPost)) {

					return new ResponseEntity<Post>(tempPost, HttpStatus.OK);

				} else {

					return new ResponseEntity<ErrorClass>(new ErrorClass(21, "Post Updation failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(22, "User must be logged in to update Post"),
					HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/deletePostById/{PostId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePostById(@PathVariable("PostId") int PostId, HttpSession session) {
		log.info("Delete Posts: Delete Post By Id " + PostId);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.getUserById(userId);
			if (user.isEnabled()) {
				Post tempPost = postService.getPost(PostId);
				if (postService.deletePost(tempPost)) {
					return new ResponseEntity<Post>(tempPost, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(new ErrorClass(23, "Post Deletion failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(24, "User must be logged in to delete Post"),
					HttpStatus.CONFLICT);

		}
	}

	@RequestMapping(value = "/updatePostComment", method = RequestMethod.POST)
	public ResponseEntity<?> updatePostComment(@RequestBody Comment comment, HttpSession session) {
		log.info("UpdatePostComment: Post comment Post Id= " + comment.getId());
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.getUserById(userId);
			if (user.isEnabled()) {
				comment.setCommentedOn(new Date());

				if (postCommentService.updatePostComment(comment)) {
					return new ResponseEntity<Comment>(comment, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(new ErrorClass(19, "Post Comment Updation failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(12, "User must be logged in to add a comment"),
					HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/deletePostComment", method = RequestMethod.POST)
	public ResponseEntity<?> deletePostComment(@RequestBody Comment comment, HttpSession session) {
		log.info("Delete PostComment: Post comment Post Id= ");
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return new ResponseEntity<ErrorClass>(new ErrorClass(7, "User session details not found"),
					HttpStatus.UNAUTHORIZED);
		} else {
			User user = userService.getUserById(userId);
			if (user.isEnabled()) {

				if (postCommentService.deletePostComment(comment)) {
					return new ResponseEntity<Comment>(comment, HttpStatus.OK);
				} else {
					return new ResponseEntity<ErrorClass>(new ErrorClass(19, "Post Comment Deletion failed"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<ErrorClass>(new ErrorClass(12, "User must be logged in to add a comment"),
					HttpStatus.CONFLICT);
		}
	}
}
