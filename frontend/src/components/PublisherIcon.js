import React from 'react';

import { ReactComponent as HomeIcon } from './icons/home.svg';
import { ReactComponent as SearchIcon } from './icons/search.svg';
import { ReactComponent as UserIcon } from './icons/user.svg';

// 아이콘 맵핑
const ICON_MAP = {
  home: HomeIcon,
  search: SearchIcon,
  user: UserIcon,
};

const PublisherIcon = ({ name }) => {
  const SelectedIcon = ICON_MAP[name];

  if (!SelectedIcon) {
    return <div style={{ width: '25px', height: '25px' }}>Invalid Icon</div>;
  }

  return <SelectedIcon style={{ width: '25px', height: '25px' }} />;
};

export default PublisherIcon;
