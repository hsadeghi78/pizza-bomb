import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PUBLIC_ROUTE } from './public.route';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { CriticismsComponent } from './criticisms/criticisms.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(PUBLIC_ROUTE)],
  declarations: [ContactUsComponent, AboutUsComponent, CriticismsComponent],
})
export class PublicModule {}
