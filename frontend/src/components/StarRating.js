import React, { useState } from "react";
import { FaStar, FaStarHalfAlt } from "react-icons/fa";
import styled from "styled-components";

const RowBox = styled.div`
  display: flex;
  align-items: center;
`;

const StarDiv = styled.div`
  position: relative;
  cursor: pointer;
  margin-right: 5px;
  display: flex;
  align-items: center;
`;

const HalfStarOverlay = styled.div`
  position: absolute;
  width: 50%;
  height: 100%;
  top: 0;
  z-index: 1;
`;

const Left = styled(HalfStarOverlay)`
  left: 0;
`;

const Right = styled(HalfStarOverlay)`
  right: 0;
`;

const RatingDisplay = styled.span`
  margin-left: 10px;
  font-size: 18px;
  color: black;
`;

function StarRating({ rating, onRatingChange }) {
  const [score, setScore] = useState(rating);
  const [scoreFixed, setScoreFixed] = useState(rating);

  const handleLeftHalfEnter = (idx) => setScore(idx + 0.5);
  const handleRightHalfEnter = (idx) => setScore(idx + 1);

  const handleStarClick = () => {
    setScoreFixed(score);
    onRatingChange(score); // 평가 변경 시 부모에게 알림
  };

  const handleStarLeave = () => {
    setScore(scoreFixed);
  };

  return (
    <RowBox>
      {Array(5)
        .fill(0)
        .map((_, idx) => (
          <StarDiv key={idx}>
            {/* 반만 채워진 별 */}
            {score - Math.floor(score) === 0.5 && Math.floor(score) === idx ? (
              <FaStarHalfAlt size={32} color="gold" />
            ) : idx + 1 > score ? (
              <FaStar size={32} color="lightGray" />
            ) : (
              <FaStar size={32} color="gold" />
            )}
            <Left
              onMouseEnter={() => handleLeftHalfEnter(idx)}
              onMouseLeave={handleStarLeave}
              onClick={handleStarClick}
            />
            <Right
              onMouseEnter={() => handleRightHalfEnter(idx)}
              onMouseLeave={handleStarLeave}
              onClick={handleStarClick}
            />
          </StarDiv>
        ))}
      <RatingDisplay>{score.toFixed(1)}</RatingDisplay>
    </RowBox>
  );
}

export default StarRating;
