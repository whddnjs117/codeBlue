package com.example.codeblue.vo;

import lombok.Data;

@Data
public class QuestionBoard {

	private int questionId;
	private String questionTitle;
	private String questionContent;
	private String questionDatetime;
	private int answerCount;
	private int voteCount;
	private int commentCount;
	private String questionTags;
	
	//외래키 3개
	private Feild feild;
	private User user;
	private Tag tag;
}