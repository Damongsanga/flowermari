import { StyledAvatar, AvatarImage } from '../avatar/StyledAvartar.tsx'

interface AvatarProps {
  link?: string;
}

export const Avatar = ({ link }: AvatarProps) => {
  return (
    <>
    <StyledAvatar>
      <AvatarImage src={link} alt="img" />
    </StyledAvatar>
    </>
  );
};
