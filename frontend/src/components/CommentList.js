// src/components/CommentList.js
import React from 'react';
import styles from './CommentList.module.css';

const CommentList = ({ comments }) => {
  if (!comments || comments.length === 0) {
    return <div className={styles['no-comments']}>아직 코멘트가 없습니다.</div>;
  }

  return (
    <div className={styles['comment-list']}>
      {comments.map((comment) => (
        <div key={comment.id} className={styles['comment-item']}>
          <div className={styles['comment-author']}>{comment.author}</div>
          <div className={styles['comment-text']}>{comment.text}</div>
        </div>
      ))}
    </div>
  );
};

export default CommentList;
