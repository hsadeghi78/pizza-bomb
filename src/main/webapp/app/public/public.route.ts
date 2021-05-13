import { Route } from '@angular/router';
import { ContactUsComponent } from 'app/public/contact-us/contact-us.component';
import { CriticismsComponent } from 'app/public/criticisms/criticisms.component';
import { AboutUsComponent } from 'app/public/about-us/about-us.component';
import { NewsComponent } from 'app/public/news/news.component';

export const PUBLIC_ROUTE: Route[] = [
  {
    path: 'about-us',
    component: AboutUsComponent,
    data: {
      pageTitle: 'global.menu.public.about-us',
    },
  },
  {
    path: 'contact-us',
    component: ContactUsComponent,
    data: {
      pageTitle: 'global.menu.public.contact-us',
    },
  },
  {
    path: 'criticisms',
    component: CriticismsComponent,
    data: {
      pageTitle: 'global.menu.public.criticisms',
    },
  },
  {
    path: 'news',
    component: NewsComponent,
    data: {
      pageTitle: 'global.menu.public.news',
    },
  },
];
