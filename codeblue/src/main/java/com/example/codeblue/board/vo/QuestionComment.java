package com.example.codeblue.board.vo;

import com.example.codeblue.user.vo.User;

import lombok.Data;

@Data
public class QuestionComment {
	private int questionCommentId;
	private User user;
	private String questionCommentContent;
	private QuestionBoard questionBoard;
	private String questionCommentDatetime;
}