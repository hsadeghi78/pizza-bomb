import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PUBLIC_ROUTE } from './public.route';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { CriticismsComponent } from './criticisms/criticisms.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { NewsComponent } from './news/news.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(PUBLIC_ROUTE), LeafletModule],
  declarations: [ContactUsComponent, AboutUsComponent, CriticismsComponent, NewsComponent],
})
export class PublicModule {}
