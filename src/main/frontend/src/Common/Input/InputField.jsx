import React, { useEffect } from 'react'

import style from "./InputField.module.css";
import SearchIcon from "../../assets/images/search/search.svg";

const InputField = () => {

  useEffect(() => {
    const search = async () => {
      
    }
  });

  return (
    <div className={style.Wrapper}>
      <div className={style.SearchContainer} >
        <div className={style.icon}>
          <img src={SearchIcon} alt="검색 아이콘" />
        </div>
        <input
          className={style.SearchBar} 
          placeholder="제목/저자를 입력해주세요!"
        />
        
      </div>
    </div>
  ); 
};

export default InputField;