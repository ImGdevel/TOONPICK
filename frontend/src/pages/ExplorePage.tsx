import React, { useState, useEffect } from 'react';
import { ExplorePageState } from '../types/page';
import WebtoonService from '../services/webtoonService';
import WebtoonGrid from '../components/WebtoonGrid';
import styles from './ExplorePage.module.css';

const ExplorePage: React.FC = () => {
  const [state, setState] = useState<ExplorePageState>({
    categories: [],
    selectedCategory: null,
    webtoons: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await WebtoonService.getWebtoonsByCategory(state.selectedCategory || 'ALL');
        setState(prev => ({
          ...prev,
          categories: response.data,
          isLoading: false
        }));
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '카테고리를 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchCategories();
  }, []);

  useEffect(() => {
    const fetchWebtoons = async () => {
      if (!state.selectedCategory) return;

      try {
        setState(prev => ({ ...prev, isLoading: true }));
        const response = await WebtoonService.getWebtoonsByCategory(state.selectedCategory);
        setState(prev => ({
          ...prev,
          webtoons: response.data,
          isLoading: false
        }));
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '웹툰을 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchWebtoons();
  }, [state.selectedCategory]);

  return (
    <div className={styles.explorePage}>
      <h1>웹툰 탐색</h1>
      
      <div className={styles.categories}>
        {state.categories.map(category => (
          <button
            key={category}
            className={`${styles.categoryButton} ${
              state.selectedCategory === category ? styles.selected : ''
            }`}
            onClick={() => setState(prev => ({ ...prev, selectedCategory: category }))}
          >
            {category}
          </button>
        ))}
      </div>

      {state.isLoading ? (
        <div>로딩중...</div>
      ) : state.error ? (
        <div>{state.error}</div>
      ) : (
        <WebtoonGrid webtoons={state.webtoons} />
      )}
    </div>
  );
};

export default ExplorePage;