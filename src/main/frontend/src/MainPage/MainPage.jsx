import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import layout from '../Common/Layout.module.css'
import main from './MainPage.module.css'
import InputField from '../Common/Input/InputField.jsx'
import HomeHeader from '../Common/HomeHeader/HomeHeader.jsx'

export default function MainPage() {
    

    return (
        <div className={ layout.layout }>
            <HomeHeader/>
            <InputField/>
        </div>
    );
};
