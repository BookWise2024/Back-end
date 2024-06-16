import { useState } from 'react'
import styles from './subMenu.module.css'
import layout from './Layout.module.css'
import img from '../images/submenu/bookicon.svg'
import img2 from '../images/submenu/compareicon.svg'
import logoutimg from '../images/submenu/logout.svg'

export default function SubMenu() {
    const [email, setEmail] = useState("로그인이 필요합니다");

    const title = [
        {key: 1, name: '내 선호책 리스트', image: img}, 
        {key: 2, name: '도서 비교하기', image: img2}, 
        {key: 3, name: '도서관 조회하기', image: img2}
    ]
    const lis = []
    const log = []

    for(let i = 0; i < title.length; i++) {
        lis.push(
            <div key={ title[i].key } className={ styles.menulist }>
                <div className={ styles.menuicon }>
                    <img className={ styles.icon } src={title[i].image}/>
                </div>
                <div className={ styles.menutext }>{ title[i].name }</div>
            </div>
        )
    }

    log.push(
        <div className={ styles.logoutbutton }>
            <div className={ styles.logouticon }>
                <img className={ styles.secicon } src={ logoutimg }/>
            </div>
            <div className={ styles.menutext }>로그아웃</div>
        </div>
    )

    return(
        <div className={ layout.oplayout }>
            <div className={ styles.sublayout }>
                <div className={ styles.identity }>{ email }</div>
                <div className={ styles.divide }></div>
                <div className={ styles.submenu }>
                    {lis}
                </div>
                {log}
            </div>
        </div>
    )
}

