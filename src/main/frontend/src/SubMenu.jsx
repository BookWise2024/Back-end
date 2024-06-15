import { useState } from 'react'
import styles from './subMenu.module.css'
import layout from './Layout.module.css'
import img from '../images/submenu/bookicon.svg'
import img2 from '../images/submenu/compareicon.svg'

export default function SubMenu() {
    const title = [
        {name: '내 선호책 리스트', image: img}, 
        {name: '도서 비교하기', image: img2}, 
        {name: '도서관 조회하기', image: img2}
    ]
    const lis = []

    for(let i = 0; i < title.length; i++) {
        lis.push(
            <div className={ styles.menulist }>
                <div className={ styles.menuicon }>
                    <img className={ styles.icon } src={title[i].image}/>
                </div>
                <div className={ styles.menutext }>{ title[i].name }</div>
            </div>
        )
    }
    
    return(
        <div className={ layout.layout }>
            <div className={ styles.sublayout }>
                <div className={ styles.identity }>Temp</div>
                <div className={ styles.divide }></div>
                <div className={ styles.submenu }>
                    {lis}
                </div>
            </div>
        </div>
    )
}

