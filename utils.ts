export const getApiUrl = (path: string): string => {
  let base = '';
  if (typeof window !== 'undefined') {
    const savedBase = localStorage.getItem('gasino_api_server_url');
    if (savedBase && savedBase.trim() !== '') {
      base = savedBase.trim();
    } else {
      const origin = window.location.origin;
      if (origin && !origin.includes('file://') && !origin.includes('localhost') && !origin.includes('127.0.0.1')) {
        base = origin;
      } else {
        // Fallback to the production deployed URL
        base = 'https://ais-pre-paup5q3fn37ypcgv6bevqn-56010228689.us-east1.run.app';
      }
    }
  } else {
    base = 'https://ais-pre-paup5q3fn37ypcgv6bevqn-56010228689.us-east1.run.app';
  }
  
  const cleanBase = base.replace(/\/+$/, '');
  const cleanPath = path.replace(/^\/+/, '');
  return `${cleanBase}/${cleanPath}`;
};

export const getProxiedImageUrl = (url: string): string => {
  if (!url) return url;
  const isExternal = url.startsWith('http://') || url.startsWith('https://');
  if (isExternal && !url.includes('api/proxy-image')) {
    return getApiUrl(`/api/proxy-image?url=${encodeURIComponent(url)}`);
  }
  return url;
};
