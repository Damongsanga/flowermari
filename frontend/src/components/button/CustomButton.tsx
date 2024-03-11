import React from 'react';
import styled, {css} from 'styled-components';

interface ButtonProps {
    size?: 'small' | 'medium' | 'large';
    $make?: boolean;
    $check?: boolean;
    $kakao?: boolean;
    onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
    children: React.ReactNode;
}

const StyledButton = styled.button<ButtonProps>`
    border-radius: 30px;
    border: none;
    outline: none;
    text-align: center;
    margin-top: 5px;
    transition: box-shadow 0.3s ease, background-color 0.3s ease;

    ${({size}) => {
        switch (size) {
            case 'small':
                return css`
                    width: 80px;
                    height: 28px;
                    font-size: 12px;
                    line-height: 28px;
                `;
            case 'medium':
                return css`
                    width: 100px;
                    height: 35px;
                    font-size: 14px;
                    line-height: 35px;
                `;
            case 'large':
                return css`
                    width: 120px;
                    height: 40px;
                    font-size: 16px;
                    line-height: 40px;
                `;
        }
    }}

    ${({$make}) => $make && css`
        background: #FFF6F2;
    `}

    ${({$check}) => $check && css`
        background: #FFFFFF;
        box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    `}

    ${({$kakao}) => $kakao && css`
        width: 472px;
        height: 79px;
        background: #FFE812;
        box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.1), 0px 1px 2px rgba(0, 0, 0, 0.06);
        border-radius: 8px;
    `}
`;

const CustomButton: React.FC<ButtonProps> = ({
                                                 children,
                                                 size = 'medium',
                                                 $make,
                                                 $check,
                                                 $kakao,
                                                 onClick,
                                             }) => (
    <StyledButton
        size={size}
        $make={$make}
        $check={$check}
        $kakao={$kakao}
        onClick={onClick}
    >
        {children}
    </StyledButton>
);

export default CustomButton;
