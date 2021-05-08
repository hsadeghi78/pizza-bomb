import { Route } from '@angular/router';
import { ContactUsComponent } from 'app/public/contact-us/contact-us.component';
import { CriticismsComponent } from 'app/public/criticisms/criticisms.component';
import { AboutUsComponent } from 'app/public/about-us/about-us.component';

export const PUBLIC_ROUTE: Route[] = [
  {
    path: 'about-us',
    component: AboutUsComponent,
    data: {
      pageTitle: 'home.title',
    },
  },
  {
    path: 'contact-us',
    component: ContactUsComponent,
    data: {
      pageTitle: 'home.title',
    },
  },
  {
    path: 'criticisms',
    component: CriticismsComponent,
    data: {
      pageTitle: 'home.title',
    },
  },
];
