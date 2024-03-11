import { StyledMenu } from "./StyledMenu";
import { Avatar } from "../avatar/Avatar";

interface MenuProps {
  link?: string;
}

export const Menu = ({ link }: MenuProps) => {
  return (
    <StyledMenu>
      <Avatar link={link}></Avatar>
    </StyledMenu>
  );
};