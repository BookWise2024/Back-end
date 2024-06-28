import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
// ------------------------------------------------------
import layout from '../Common/Layout.module.css'
import mainStyle from './MainPage.module.css'
import AppStyle from "../App.module.css";
// ------------------------------------------------------
import InputField from '../Common/Input/InputField.jsx'
import HomeHeader from '../Common/HomeHeader/HomeHeader.jsx'
import MainBookList from './BookList/MainBookList.jsx';
import SubBookList from './BookList/SubBookList.jsx';


export default function MainPage() {

    return (
        <div className={ layout.layout }>
            <HomeHeader/>
            <InputField/>
            <div className={mainStyle.container}>
                <MainBookList/>
                {/* <SubBookList/> */}
            </div>
        </div>
    );
};
